from flask import Flask, request
import pickle
import json
import numpy as np
from tensorflow import keras
from tensorflow.keras.preprocessing.sequence import pad_sequences


from Sastrawi.Stemmer.StemmerFactory import StemmerFactory
from Sastrawi.StopWordRemover.StopWordRemoverFactory import StopWordRemoverFactory
import re 

app = Flask(__name__)

model = keras.models.load_model('model/bangkitcapstone.h5')
tokenizer = pickle.load(open('model/bangkitcapstone.pickle', 'rb'))
label_class = ['parcels','groceries','foods','cookies','snacks','frozen','drinks']
#Method GET 
@app.route('/')
def welcome():
 return "Hai, Selamat datang di elevate! recommender system."

@app.route('/predict', methods=['POST'])
def predict():
 request_json = request.json

 data = request_json['data']
 id_data = request_json['id']

 text_prep = preprocessing(data)
 #tokenizing newdata
 newdata = tokenizer.texts_to_sequences(text_prep)
 newdata = pad_sequences(newdata, maxlen = 14)

 model_predict = model.predict(newdata)
 model_label = np.argmax(model_predict, axis = 1)
 model_probability = model_predict.max(axis = 1)

 #proccess model into prediction and probability of newdata
 result = []
 id_result = []
 prob = []
 i=0
 for text, model_label, model_probability in zip(data,model_label,model_probability):
  print('"{}"'.format(text))
  l = label_class[model_label]

  if l == request_json['filter']:
   result.append(text)
   id_result.append(id_data[i])
   prob.append(str(model_probability))
  

  print("  - Prediction: '{}'".format(label_class[model_label]))
  print("  - Probability: '{}'".format(model_probability))
  print("")
  i+=1
 
 
 response_json = {
  "id": list(id_result),
  "prediction" : list(result),
  "probability": list(prob)
 }
 
 return json.dumps(response_json)


# Fungsi preprocessing berfngsi untuk melakukan preporoes pada text
def preprocessing (X_train):
 #temp array
 documents = []
 factory = StopWordRemoverFactory()
 stopword = factory.create_stop_word_remover()
 factory1 = StemmerFactory()
 stemmer = factory1.create_stemmer()
 #loop text
 for sentence in X_train:
  print(sentence[0])
  document = sentence[0].lower() # ubah jadi huruf kecil
  document = re.sub(r"\d+", "", document) # hapus semua angka
  document = re.sub(r'\W', ' ', document) # hapus spesial karakter
  document = re.sub(r'\s+', ' ', document, flags=re.I) # hapus multiple space
  document = stopword.remove(document) # hapus stopword
  document = stemmer.stem(document) # stemming

  documents.append(document) #store hasil prepcoses pada array

 return documents

if __name__ == '__main__':
 #app.run(debug=True)
 app.run(host='0.0.0.0', port=5000)