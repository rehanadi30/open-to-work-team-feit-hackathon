from inference_sdk import InferenceHTTPClient
import torch
from PIL import Image
from transformers import AutoImageProcessor, AutoModelForImageClassification


def generate_food_item(image):

    CLIENT = InferenceHTTPClient(
    api_url="https://detect.roboflow.com",
    api_key="W0jdPT5uPh5h2LesYMkY"
    )
    print("yooo")
    result = CLIENT.infer(image, model_id="ingredient-detection-hcmut/2")
    print("hiii")
    # Extract only the unique 'class' values
    class_items = list(set([prediction['class'] for prediction in result['predictions']]))
    print(class_items)

    return class_items


if __name__ == "__main__":

    input_image = 'fridge.png'
    input_path = 'backend/images/' + input_image

    #Extract only the unique 'class' values
    result = generate_food_item(input_path)

    
    