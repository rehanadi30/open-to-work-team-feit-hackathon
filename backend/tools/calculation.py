import pandas as pd
from nltk.corpus import wordnet as wn


def singularize(word):
    """Convert plural nouns to singular."""
    # Check if the word has a plural form and get the singular form
    singular_forms = [lemma.name() for syn in wn.synsets(word) for lemma in syn.lemmas() if lemma.name().endswith('s')]
    return singular_forms[0] if singular_forms else word

# Load the carbon emission dataset
carbon_df = pd.read_csv('../data/emission_factor.csv')
carbon_df['Food Items'] = carbon_df['Food Items'].str.lower()

# Load the calories dataset
calories_df = pd.read_csv('../data/calories.csv')
calories_df['FoodItem'] = calories_df['FoodItem'].str.lower().apply(singularize)


def carbon_calculation(items_to_check):

    # Normalize the given list
    normalized_items = [item.lower() for item in items_to_check]

    # Calculate the total emission for the given items
    total_emission = carbon_df[carbon_df['Food Items'].isin(normalized_items)]['Emission Factor'].sum()

    # Display the result
    print(f"Total carbon emission: {total_emission}")

    return total_emission


def calories_calculation(items_to_check):

    # Normalize the given list
    normalized_items = [item.lower() for item in items_to_check]

    total_calories = calories_df[calories_df['FoodItem'].isin(normalized_items)]['Cals_per100grams'].str.replace(' cal', '').astype(int).sum()
    print(f"Total calories for specified food items: {total_calories}")

    return total_calories



if __name__ == "__main__":

    # Given list of food items
    items_to_check = ['potato', 'banana', 'onion', 'milk']
    
    #carbon
    total_emission = carbon_calculation(items_to_check)

    #calories
    total_calories = calories_calculation(items_to_check)
