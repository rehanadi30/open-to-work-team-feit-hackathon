from flask import Flask, request, jsonify
from PIL import Image
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
from tools.detection_pure import generate_food_item
from tools.calculation import carbon_calculation,calories_calculation
import re
import io

app = Flask(__name__)

# Load the T5 recipe generation model and tokenizer
tokenizer = AutoTokenizer.from_pretrained("flax-community/t5-recipe-generation")
model = AutoModelForSeq2SeqLM.from_pretrained("flax-community/t5-recipe-generation")

# Generate food from input image
@app.route('/generate-food', methods=['POST'])
def generate_food_from_image():
    
    food_image_file = request.files.get("image")
    if not food_image_file:
        return jsonify({"error": "No image provided"}), 400

    try:
        # Read the image file into a byte stream
        image_stream = io.BytesIO(food_image_file.read())
        # Open the image using PIL
        image = Image.open(image_stream)
        
        # Convert the image to RGB if it is in RGBA mode
        if image.mode == 'RGBA':
            image = image.convert('RGB')

        food_items = generate_food_item(image)
        
        response = {
            'items': food_items
        }
        # Return the JSON response
        return jsonify(response), 200
    
    except Exception as e:
        return jsonify({"error": f"An error occurred: {str(e)}"}), 500

# Calculate carbon emission and calories
@app.route('/calculation', methods=['POST'])
def carbon_calories_calculation():
    
    food_items = request.get_json()
    if not food_items:
        return jsonify({"error": "Invalid input"}), 400
    try:
        #DUMMY 

        carbon_emission = carbon_calculation(food_items)
        calories = calories_calculation(food_items)

        response = {
            'total carbon emission': carbon_emission,
            'total calories': calories
        }
        # Return the JSON response
        return jsonify(response)

    except Exception as e:
        return jsonify({"error": f"An error occurred: {str(e)}"}), 500


@app.route('/generate-recipe', methods=['POST'])
def generate_recipe():
    data = request.json
    available_ingredients = data.get("ingredients", "")
    
    if not available_ingredients:
        return jsonify({"error": "No ingredients provided"}), 400

    # Generate a prompt for the model
    prompt = f"Generate a recipe given these ingredients: {available_ingredients}"

    # Tokenize the input prompt
    input_ids = tokenizer.encode(prompt, return_tensors="pt")

    # Generate the output
    output_ids = model.generate(input_ids, max_length=300)

    # Decode the generated text
    generated_text = tokenizer.decode(output_ids[0], skip_special_tokens=True)

    # Parse the generated text into structured format
    structured_recipe = parse_recipe(generated_text)

    # Return both the structured recipe and the raw generated text
    return jsonify({
        "raw_response": generated_text,
        "structured_recipe": structured_recipe
    })

def parse_recipe(text):
    # Use regex to match the title, ingredients, and directions
    title_match = re.search(r'^title:\s*(.*?)\s*ingredients:', text, re.IGNORECASE)
    title = title_match.group(1).strip() if title_match else "Untitled Recipe"

    ingredients_match = re.search(r'ingredients:\s*(.*?)\s*directions:', text, re.IGNORECASE)
    ingredients = ingredients_match.group(1).strip().split(', ') if ingredients_match else []

    directions_match = re.search(r'directions:\s*(.*)', text, re.IGNORECASE)
    steps = directions_match.group(1).strip().split('. ') if directions_match else []

    # Clean up steps to remove empty strings and whitespace
    steps = [step.strip() for step in steps if step]

    return {
        "title": title,
        "ingredients": ingredients,
        "steps": steps
    }

if __name__ == '__main__':
    app.run(debug=True)
