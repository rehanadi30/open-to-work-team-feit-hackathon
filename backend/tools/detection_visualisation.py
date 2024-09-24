from inference_sdk import InferenceHTTPClient
from roboflow import Roboflow
import supervision as sv
import cv2


input_image = 'fridge.png'
input_path = 'images/' + input_image

rf = Roboflow(api_key="W0jdPT5uPh5h2LesYMkY")
project = rf.workspace().project("ingredient-detection-hcmut")
model = project.version(2).model

result = model.predict(input_path, confidence=40, overlap=30).json()

labels = [item["class"] for item in result["predictions"]]

detections = sv.Detections.from_inference(result)

label_annotator = sv.LabelAnnotator()
bounding_box_annotator = sv.BoxAnnotator()

image = cv2.imread(input_path)

annotated_image = bounding_box_annotator.annotate(
    scene=image, detections=detections)
annotated_image = label_annotator.annotate(
    scene=annotated_image, detections=detections, labels=labels)

output_path = "output/" + input_image
cv2.imwrite(output_path,annotated_image)
