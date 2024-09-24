from flask import Flask, request, jsonify
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
import re

app = Flask(__name__)

# Load the T5 recipe generation model and tokenizer
tokenizer = AutoTokenizer.from_pretrained("flax-community/t5-recipe-generation")
model = AutoModelForSeq2SeqLM.from_pretrained("flax-community/t5-recipe-generation")

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
