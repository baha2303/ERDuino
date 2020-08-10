const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

// exports.helloWorld = functions.https.onRequest((request, response) => {
//  console.log("Hello World Message");
//  response.send("Hello from Firebase!");

//  // Get a database reference to our posts
// var db = admin.database();
// var ref = db.ref("travelMode");

// // Attach an asynchronous callback to read the data at our posts reference
// ref.on("value", function(snapshot) {
//   console.log(snapshot.val());
// }, function (errorObject) {
//   console.log("The read failed: " + errorObject.code);
// });




  exports.update = functions.https.onRequest(async (req, res) => {

    //https://us-central1-eruino-1b4be.cloudfunctions.net/updateSensor?gas=10&flame=20&water=30&motion=40&fan=OFF&window=OPEN&travelMode=ON&lift=DOWN

    // Grab the text parameter.
    const gas_sensor = req.query.gas;
    const flame_sensor = req.query.flame;
    const water_sensor = req.query.water;
    const motion_sensor = req.query.motion;
    const FAN = req.query.fan;
    const WINDOW = req.query.window;
    //const TRAVEL = req.query.travelMode;
    const LIFT = req.query.lift;

    // Push the new sensor value into the Realtime Database using the Firebase Admin SDK.
    const snapshot1 = await admin.database().ref(`/sensors/gasSensor`).set(gas_sensor);
    const snapshot2 = await admin.database().ref(`/sensors/flameSensor`).set(flame_sensor);
    const snapshot3 = await admin.database().ref(`/sensors/motionSensor`).set(motion_sensor);
    const snapshot4 = await admin.database().ref(`/sensors/waterSensor`).set(water_sensor);
    const snapshot5 = await admin.database().ref(`/gear/fan`).set(FAN);
    const snapshot6 = await admin.database().ref(`/gear/lift`).set(LIFT);
    //const snapshot7 = await admin.database().ref(`/gear/travelMode`).set(TRAVEL);
    const snapshot8 = await admin.database().ref(`/gear/windows`).set(WINDOW);
    
    res.json({result: `VALUES UPDATED.`});
  });



exports.get = functions.https.onRequest((request, response) => {

  // Get a database reference to our posts
 const _switch = request.query.switch;
 var db = admin.database();
 var ref = db.ref(_switch);
 
 // Attach an asynchronous callback to read the data at our posts reference
 ref.once("value", function(snapshot) {
   console.log(snapshot.val());
   response.json(snapshot.val());
 }, function (errorObject) {
   console.log("The read failed: " + errorObject.code);
 });
 });