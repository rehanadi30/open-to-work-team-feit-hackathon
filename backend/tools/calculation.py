import pandas as pd
from nltk.corpus import wordnet as wn


def singularize(word):
    """Convert plural nouns to singular."""
    # Check if the word has a plural form and get the singular form
    singular_forms = [lemma.name() for syn in wn.synsets(word) for lemma in syn.lemmas() if lemma.name().endswith('s')]
    return singular_forms[0] if singular_forms else word

# Load the carbon emission dataset
carbon_df = pd.read_csv('data/emission_factor.csv')
carbon_df['Food Items'] = carbon_df['Food Items'].str.lower()

# Load the calories dataset
calories_df = pd.read_csv('data/calories.csv')
calories_df['FoodItem'] = calories_df['FoodItem'].str.lower().apply(singularize)
calories_df['Cals_per100grams'] = calories_df['Cals_per100grams'].str.replace(' cal', '').str.strip().astype(int)


def carbon_calculation(food_masses):
    
    total_emission = 0

    for item in food_masses:
        food_name = item['Food']
        mass = item['Mass (g)'] / 1000

        # Normalize the food name for comparison
        normalized_food_name = food_name.lower()
        
        # Get the calorie content per 100 grams
        emission_factor = carbon_df[carbon_df['Food Items'].str.lower() == normalized_food_name]['Emission Factor']
        
        if not emission_factor.empty:
            # Calculate calories based on the mass
            total_emission += emission_factor.values[0] * mass
    
    print(f"Total carbon emission for your food: {total_emission:.2f}")

    return total_emission


def calories_calculation(food_masses):
    
    total_calories = 0

    for item in food_masses:
        food_name = item['Food']
        mass = item['Mass (g)']

        # Normalize the food name for comparison
        normalized_food_name = food_name.lower()
        
        # Get the calorie content per 100 grams
        calorie_per_100g = calories_df[calories_df['FoodItem'].str.lower() == normalized_food_name]['Cals_per100grams']
        
        if not calorie_per_100g.empty:
            # Calculate calories based on the mass
            calories_for_item = (calorie_per_100g.values[0] / 100) * mass
            total_calories += calories_for_item
    
    print(f"Total calories for your food: {total_calories:.2f}")

    return total_calories





if __name__ == "__main__":

    # Given list of food items
    items_to_check = ['potato', 'banana', 'onion', 'milk']
    
    #carbon
    total_emission = carbon_calculation(items_to_check)

    #calories
    total_calories = calories_calculation(items_to_check)
