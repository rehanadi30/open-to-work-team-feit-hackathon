to install run this command
    
    python ingredient.py


you can access the APIs from (use POST) as below:
    
--------------------- GENERATE RECIPE -----------------------------

    http://127.0.0.1:5000/generate-recipe'

    {
    "ingredients": "chicken, rice, broccoli"
    }


--------------------- GENERATE FOOD FROM IMAGES -----------------------------

    http://127.0.0.1:5000/generate-food'

    [Insert any food image with key 'image']


--------------------- CALCULATE CARBON / CALORIES -----------------------------

    http://127.0.0.1:5000/calculation'

    [
    {"Food": "Apple", "Mass (g)": 150},
    {"Food": "Banana", "Mass (g)": 120},
    {"Food": "Orange", "Mass (g)": 130},
    {"Food": "Potato", "Mass (g)": 200},
    {"Food": "Tomato", "Mass (g)": 100}
    ]
