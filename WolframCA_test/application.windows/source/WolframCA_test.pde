// Wolfram Cellular Automata
// modified code from Daniel Shiffman <http://www.shiffman.net>

// Simple demonstration of a Wolfram 1-dimensional cellular automata
// with the system scrolling by
// Also implements wrap around

import org.json.*;

String BASE_URL = "http://127.0.0.1:5000";
String galapaguserName;
ArrayList<Integer> traits;
JSONArray galapagusers;
Timer timer;
int randomUser;

CA ca;   // An object to describe a Wolfram elementary Cellular Automata

void setup() {
  size(200, 200);
  background(255);
  randomUser = 0;
  traits = new ArrayList<Integer>();
  //int[] ruleset = {0, 1, 0, 1, 1, 0, 1, 0};   // Rule 90
  //int[] ruleset = {0,1,1,1,1,0,1,1};           // Rule 222  
  //int[] ruleset = {0,1,1,1,1,1,0,1};           // Rule 190  
  //int[] ruleset = {0,1,1,1,1,0,0,0};           // Rule 30  
  //int[] ruleset = {0,1,1,1,0,1,1,0};             // Rule 110
  
  // Rule 90 repeated, w/ base 3 to capture color variable
  int[] ruleset = {
    0, 1, 0, 2, 1, 0, 1, 0, 0, 2, 0, 1, 2, 0, 2, 0, 0, 1, 0, 1, 2, 0, 1, 0, 0, 2, 0
  };

  // hits /jsonSingle route in nodejs express, grabs single JSON object w/ userID of 0
  getImportData();

  // checks /jsonSingle route every 5 seconds (so don't spam an external site!)
  timer = new Timer(5000);
  timer.start();

  ca = new CA(ruleset);                 // Initialize CA
}

void draw() {
  ca.display();          // Draw the CA
  ca.generate();
  fill(0);
  text(galapaguserName, 5, 15);

  //check timer
  if (timer.isFinished()) {
    println("timer is finished");
    getImportData();
    timer.start();
  }
}

JSONObject fetchJSON(String URL_PATH) {
  // Get the JSON formatted response
  String response = loadStrings(BASE_URL + URL_PATH)[0];

  // Make sure we got a response.
  if ( response != null ) {
    // Initialize the JSONObject for the response
    JSONObject jsonRoot = new JSONObject( response );
    println("inside fetchJSON");
    println(jsonRoot.toString());
    return jsonRoot;
  } 
  else {
    return null;
  }
}

// will give an error since /json's returned JSON has no objects with userID 0
void getMainData() {
  String mainURL = "/json";
  JSONObject jsonData = fetchJSON(mainURL);

  // Get the "galapagusers" JSONObject
  galapagusers = jsonData.getJSONArray("galapagusers");
  randomUser = round(random(galapagusers.length()));
  makeCA(randomUser);
}

void getImportData() {
  String mainImportURL = "/jsonSingle";
  JSONObject jsonImportData = fetchJSON(mainImportURL);

  // Get the "galapagusers" JSONObject
  galapagusers = jsonImportData.getJSONArray("importedUser");

  makeCA(0);
}

void makeCA(int userNum) {
  // fetches trait integer values from JSON object and puts them into traits ArrayList for use in CA
  JSONObject fetchedGalapaguser = (JSONObject)galapagusers.opt(userNum);
  traits.add(fetchedGalapaguser.optInt("strength"));
  traits.add(fetchedGalapaguser.optInt("intelligence"));
  traits.add(fetchedGalapaguser.optInt("wisdom"));
  traits.add(fetchedGalapaguser.optInt("charisma"));
  traits.add(fetchedGalapaguser.optInt("stamina"));
  traits.add(fetchedGalapaguser.optInt("wit"));
  traits.add(fetchedGalapaguser.optInt("humor"));
  traits.add(fetchedGalapaguser.optInt("education"));
  traits.add(fetchedGalapaguser.optInt("creativity"));
  traits.add(fetchedGalapaguser.optInt("responsibility"));
  traits.add(fetchedGalapaguser.optInt("discipline"));
  traits.add(fetchedGalapaguser.optInt("honesty"));
  traits.add(fetchedGalapaguser.optInt("religiosity"));
  traits.add(fetchedGalapaguser.optInt("entrepreneurialism"));
  traits.add(fetchedGalapaguser.optInt("appearance"));
  traits.add(fetchedGalapaguser.optInt("money"));
  traits.add(fetchedGalapaguser.optInt("gracefulness"));
  traits.add(fetchedGalapaguser.optInt("stress"));
  traits.add(fetchedGalapaguser.optInt("health"));
  traits.add(fetchedGalapaguser.optInt("luck"));
  traits.add(fetchedGalapaguser.optInt("talent_math"));
  traits.add(fetchedGalapaguser.optInt("talent_art"));
  traits.add(fetchedGalapaguser.optInt("talent_sports"));
  galapaguserName = fetchedGalapaguser.optString("name");
}

void mouseReleased() {
  getImportData();
}
