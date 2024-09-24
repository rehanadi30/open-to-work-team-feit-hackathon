# Installation
run the command below to install:
    
    python ingredient.py


# API prompt command
you can access the APIs from (use POST) as below:
    
############################# GENERATE RECIPE #####################################

    http://127.0.0.1:5000/generate-recipe

    {
    "ingredients": "chicken, rice, broccoli"
    }


####################### GENERATE FOOD FROM IMAGES ###############################

    http://127.0.0.1:5000/generate-food

    [Insert any food image with key 'image']


####################### CALCULATE CARBON / CALORIES ###############################

    http://127.0.0.1:5000/calculation

    {
    "foodItems": [
        {
            "food": "Apple",
            "mass": 150
        },
        {
            "food": "Banana",
            "mass": 120
        },
        {
            "food": "Orange",
            "mass": 130
        },
        {
            "food": "Potato",
            "mass": 200
        },
        {
            "food": "Tomato",
            "mass": 100
        }
    ]
}

