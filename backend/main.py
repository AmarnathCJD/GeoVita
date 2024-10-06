import datetime as dt
import meteomatics.api as api
import google.generativeai as genai
import os
import json
from aiohttp import web
from dotenv import load_dotenv
from pymongo import MongoClient
from aiohttp import web
import math
from aiohttp import ClientSession
import random

load_dotenv() # Load env from .env file


async def get_filtered_weather_data(lat, lon):
    coords = [(lat, lon)]
    username = os.getenv("METO_USERNAME")
    password = os.getenv("METO_PASSWORD")
    parameters = [
        "precip_24h:mm",
        "heavy_rain_warning_24h:idx",
        "wind_warning_24h:idx",
        "tstorm_warning_24h:idx",
    ]
    model = "mix"
    startdate = dt.datetime.now(dt.timezone.utc).replace(
        minute=0, second=0, microsecond=0
    )
    enddate = startdate + dt.timedelta(days=10)
    interval = dt.timedelta(hours=24)

    df = api.query_time_series(
        coords,
        startdate,
        enddate,
        interval,
        parameters,
        username,
        password,
        model=model,
    )

    genai.configure(api_key=os.getenv("GEMINI_APIKEY"))

    generation_config = {
        "temperature": 1,
        "top_p": 0.95,
        "top_k": 64,
        "max_output_tokens": 8192,
        "response_mime_type": "application/json",
    }

    model = genai.GenerativeModel(
        model_name="gemini-1.5-flash",
        generation_config=generation_config,
    )
    init_prompt = """
    Analyze the following weather data and provide a detailed summary in JSON format, highlighting the severity of weather conditions for each date. The parameters to consider are as follows:
    - Precipitation: Total rainfall in mm accumulated over the last 24 hours.
    - Heavy Rain Warning Index: A description based on the index value ranging from 0 to 3, indicating the severity of rainfall.
      - 0: No severe rainfall
      - 1: Heavy Rainfall
      - 2: Severe Heavy Rainfall
      - 3: Extreme Heavy Rainfall
    - Wind Warning Index: A description based on the index value ranging from 0 to 6, indicating the severity of wind conditions.
      - 0: No severe wind conditions (< 50km/h)
      - 1: Wind Gusts (50km/h to 64km/h)
      - 2: Squall (65km/h to 89km/h)
      - 3: Severe Squall (90km/h to 104km/h)
      - 4: Violent Squall (105km/h to 119km/h)
      - 5: Gale-Force Winds (120km/h to 139km/h)
      - 6: Extreme Gale-Force Winds (> 140km/h)
    - Thunderstorm Warning Index: A description based on the index value ranging from 0 to 4, indicating the severity of thunderstorms.
      - 0: No thunderstorms
      - 1: Thunderstorm (Occurrence of electric discharge)
      - 2: Heavy Thunderstorm (Occurrence of electric discharge with strong gale, heavy rain, or hail)
      - 3: Severe Thunderstorm (Occurrence of electric discharge with strong gale, heavy rain, or hail, with at least one warning feature)
      - 4: Severe Thunderstorm with Extreme Squalls and Heavy Rainfall (Occurrence of electric discharge with strong gale, heavy rain, or hail with at least one alerting feature)
      
      the returned JSON should have
      - "message": A brief description of the climate in 4 words or fewer.
      - "info": A more detailed explanation (up to one sentence) regarding the severity of the weather conditions based on the precipitation index, heavy rain warning index, wind warning index, and thunderstorm warning index. upto 10 words or so max.
      - "alert": You decide which alert to provide based on the weather conditions. Green, Yellow, Orange, Red. single word.
    """
    json_data = df.to_json(orient="records", date_format="iso")
    response = await model.generate_content(
        init_prompt + " Here is the json for the data: " + json_data
    )
    return response.text


async def get_weather(request):
    data = await request.json()
    lat = data.get("lat")
    lon = data.get("lon")
    result = await get_filtered_weather_data(lat, lon)
    json_data = json.loads(result)
    return (
        web.json_response(
            {
                "location": "",
                "message": json_data.get("message"),
                "info": json_data.get("info"),
                "alert": json_data.get("alert"),
            }
        )
        if result
        else web.json_response(
            {
                "location": "Unknown",
                "message": "No message",
                "info": "No info",
                "alert": "No alert",
            }
        )
    )


app = web.Application()
app.router.add_post("/weather", get_weather)

CORS_HEADERS = {
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST",
    "Access-Control-Allow-Headers": "Content-Type",
}

database = MongoClient(os.getenv("MONGO_URI"))
db = database["geovite"]

loc = db.locations
emergency = db.emergencies
recep = db.receps


async def update_user_location(request):
    data = await request.json()
    if (
        data.get("username") is None
        or data.get("lat") is None
        or data.get("lon") is None
    ):
        return web.json_response(
            {"status": "error: missing fields (need username, lat, lng)"},
            headers=CORS_HEADERS,
            status=504,
        )

    loc.update_one(
        {"username": data["username"]},
        {"$set": {"lat": data["lat"], "lng": data["lon"]}},
        upsert=True,
    )

    return web.json_response(
        {"status": "location updated"}, headers=CORS_HEADERS, status=200
    )


async def get_user_location(request):
    data = await request.json()
    if data.get("username") is None:
        return web.json_response(
            {"status": "error: missing fields (need username)"}, headers=CORS_HEADERS
        )

    location = loc.find_one({"username": data["username"]})
    if location is None:
        return web.json_response(
            {"status": "error: user does not exist"}, headers=CORS_HEADERS
        )

    return web.json_response(
        {"status": "ok", "location": location}, headers=CORS_HEADERS
    )


async def get_nearby_peoples(request):
    data = await request.json()
    if data.get("username") is None or data.get("radius") is None:
        return web.json_response(
            {"status": "error: missing fields (need username, radius)"},
            headers=CORS_HEADERS,
        )

    nearby_peoples = calculate_nearby_peoples(data["username"], data["radius"])
    return web.json_response(
        {"status": "ok", "nearby_peoples": nearby_peoples}, headers=CORS_HEADERS
    )


async def count_nearby_peoples(request):
    data = await request.json()
    if data.get("username") is None or data.get("radius") is None:
        return web.json_response(
            {"status": "error: missing fields (need username, radius)"},
            headers=CORS_HEADERS,
        )

    nearby_peoples = calculate_nearby_peoples(data["username"], data["radius"])
    return web.json_response(
        {"status": "ok", "count": len(nearby_peoples)}, headers=CORS_HEADERS
    )


def haversine(lat1, lon1, lat2, lon2):
    R = 6371.0

    lat1_rad = math.radians(lat1)
    lon1_rad = math.radians(lon1)
    lat2_rad = math.radians(lat2)
    lon2_rad = math.radians(lon2)

    dlat = lat2_rad - lat1_rad
    dlon = lon2_rad - lon1_rad

    a = (
        math.sin(dlat / 2) ** 2
        + math.cos(lat1_rad) * math.cos(lat2_rad) * math.sin(dlon / 2) ** 2
    )
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance = R * c

    return distance


def calculate_nearby_peoples(username, radius):
    location = loc.find_one({"username": username})
    if location is None:
        return []

    lat = location["lat"]
    lng = location["lng"]
    nearby_peoples = []

    for person in loc.find():
        if person["username"] != username:
            distance = haversine(lat, lng, person["lat"], person["lng"])
            if distance <= radius:
                nearby_peoples.append(
                    {"username": person["username"], "distance": distance}
                )

    return nearby_peoples


async def emergency_clearance(request):
    data = await request.json()
    if data.get("lat") is None or data.get("lng") is None:
        return web.json_response(
            {"status": "error: missing fields (need lat, lon)"}, headers=CORS_HEADERS
        )

    emergency.update_one(
        {"username": data["username"]},
        {
            "$set": {
                "lat": data["lat"],
                "lng": data["lng"],
                "active": data.get("active", True),
                "emergency_id": gen_emergency_id(),
            }
        },
        upsert=True,
    )


async def delete_emergency(request):
    data = await request.json()
    if data.get("username") is None:
        return web.json_response(
            {"status": "error: missing fields (need username)"}, headers=CORS_HEADERS
        )

    em_id = emergency.find_one({"username": data["username"]})
    if em_id is None:
        return web.json_response(
            {"status": "error: emergency does not exist"}, headers=CORS_HEADERS
        )

    emergency.delete_one({"username": data["username"]})
    recep.delete_many({"emergency_id": em_id["emergency_id"]})
    return web.json_response(
        {"status": "emergency deleted"}, headers=CORS_HEADERS, status=200
    )


async def update_recep(request):
    data = await request.json()
    if (
        data.get("crowded") is None
        or data.get("lat") is None
        or data.get("lon") is None
        or data.get("emergency_id") is None
    ):
        return web.json_response(
            {"status": "error: missing fields (need username, lat, lng)"},
            headers=CORS_HEADERS,
            status=504,
        )

    recep.update_one(
        {"username": data["username"], "emergency_id": data["emergency_id"]},
        {
            "$set": {
                "crowded": data["crowded"],
                "lat": data["lat"],
                "lng": data["lon"],
            }
        },
        upsert=True,
    )

    return web.json_response(
        {"status": "location updated"}, headers=CORS_HEADERS, status=200
    )


def gen_emergency_id():
    return str(random.randint(100000, 999999))


async def ways_data(request):
    params = request.rel_url.query
    lat, lon = params.get("lat"), params.get("lon")

    radius = 25
    top, right, bottom, left = radius_to_coords(lat, lon, radius)

    async with ClientSession() as session:
        async with session.get(
            "https://www.waze.com/live-map/api/georss?left={}&right={}&top={}&bottom={}&env=row&types=alerts,traffic".format(
                top, bottom, left, right
            ),
            headers={"User-Agent": "Mozilla/5.0"},
        ) as resp:
            data = await resp.json()

            details = []
            if data.get("alerts") is None or data.get("jams") is None:
                return web.json_response(
                    {"status": "error: no data found"}, headers=CORS_HEADERS
                )
            for item in data["alerts"]:
                if item["type"] == "ROAD_CLOSED":
                    details.append(
                        {
                            "type": "ROAD_CLOSED",
                            "lat": item["location"]["y"],
                            "lon": item["location"]["x"],
                        }
                    )

            for item in data["jams"]:
                details.append(
                    {
                        "type": "JAM",
                        "line": [
                            {"lat": point["y"], "lon": point["x"]}
                            for point in item["line"]
                        ],
                        "speed": item["speed"],
                        "street": item["street"],
                    }
                )

            return web.json_response(
                {"status": "ok", "details": details}, headers=CORS_HEADERS
            )


def radius_to_coords(lat, lon, radius):
    lat = float(lat)
    lon = float(lon)
    radius = float(radius)

    R = 6371.0
    lat_rad = math.radians(lat)
    lon_rad = math.radians(lon)

    d = radius / R

    lat1 = math.degrees(lat_rad + d)
    lon1 = math.degrees(lon_rad + d)
    lat2 = math.degrees(lat_rad - d)
    lon2 = math.degrees(lon_rad - d)

    return lat1, lon1, lat2, lon2


app.router.add_post("/update_user_location", update_user_location)
app.router.add_post("/get_user_location", get_user_location)
app.router.add_post("/get_nearby_peoples", get_nearby_peoples)
app.router.add_post("/count_nearby_peoples", count_nearby_peoples)
app.router.add_post("/emergency_clearance", emergency_clearance)
app.router.add_post("/delete_emergency", delete_emergency)
app.router.add_post("/update_recep", update_recep)
app.router.add_get("/ways_data", ways_data)

web.run_app(app, port=8080)
