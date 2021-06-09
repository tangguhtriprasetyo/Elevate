'use strict';

var functions = require('firebase-functions');
const admin = require('firebase-admin');
const dbaccess = require('@google-cloud/firestore');
admin.initializeApp();

//access firestore
const db = admin.firestore()


//dari android 
exports.testrequest = functions.https.onCall((data, context) => {
  const Bussinesscategory = data.category;
  const BusinessIdea = data.BusinessIdea;
  const uid = context.auth.uid;

  // Checking that the user is authenticated.
  if (!context.auth) {
  // Throwing an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
    'while authenticated.');
  }

 // var request = require('request');
 var realid = 0
  for(var x in body.jsonData){
    for(realid in body.jsonData){
      id = uid[realid];
      data = BusinessIdea[realid];
      realid++;
    }

 var request = require("request");
    var options = { method: 'POST',
      url: 'http://34.71.61.126:80/predict',
      headers: 
       { 'cache-control': 'no-cache',
         'content-type': 'application/json' },
      body: 
       { filter: [Bussinesscategory],
         id: [uid],
         data: 
          [BusinessIdea[uid]]},
      json: true };
  }
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
      return response;
    });

//balik dari machine learning
//resdata = res.send(response);
var resp = JSON.parse(response);
var predict = resp.prediction
return predict;

});



/*
exports.test = function(req, res) {
 //processing of received json data from source A. 
}

function sendToEndpoint(processed_data) {

  let abc = processed_data;   //send processed data to source B 

  request.post({
      uri: 'https://http://34.71.61.126/predict',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(abc)
    }); 
}*/

//ini request ke mlnya



/*
var request = require("request");
    var options = { method: 'POST',
      url: 'http://34.71.61.126:80/predict',
      headers: 
       { 'postman-token': 'a44e2963-7913-46da-d285-30820065df13',
         'cache-control': 'no-cache',
         'content-type': 'application/json' },
      body: 
       { filter: 'frozen',
         id: [ [ 1 ], [ 2 ], [ 3 ], [ 4 ], [ 5 ], [ 6 ], [ 7 ] ],
         data: 
          [ [ 'Hampers Lebaran/Idul Fitri | Suwe Ora Jamu | Healthy Digestive' ],
            [ 'lada hitam seasoned kemiri BAWANG putih' ],
            [ 'Ayam kodok Special' ],
            [ 'Kotak Exclusive Imlek untuk kue kering toples nastar, lidah kucing' ],
            [ 'Camilan seafood kerupuk TRIPANG (mentah)' ],
            [ 'Risol Kering isi bihun wortel....' ] ] },
      json: true };
    
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    });

var request = require('request'); //also tried for node-webhook, restler modules
*/
