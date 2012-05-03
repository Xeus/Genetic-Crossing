import processing.core.*; 

import org.json.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class WolframCA_test extends PApplet {

// Wolfram Cellular Automata
// modified code from Daniel Shiffman <http://www.shiffman.net>

// Simple demonstration of a Wolfram 1-dimensional cellular automata
// with the system scrolling by
// Also implements wrap around



String BASE_URL = "http://127.0.0.1:5000";
String galapaguserName;
ArrayList<Integer> traits;
JSONArray galapagusers;
Timer timer;
int randomUser;

CA ca;   // An object to describe a Wolfram elementary Cellular Automata

public void setup() {
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

public void draw() {
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

public JSONObject fetchJSON(String URL_PATH) {
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
public void getMainData() {
  String mainURL = "/json";
  JSONObject jsonData = fetchJSON(mainURL);

  // Get the "galapagusers" JSONObject
  galapagusers = jsonData.getJSONArray("galapagusers");
  randomUser = round(random(galapagusers.length()));
  makeCA(randomUser);
}

public void getImportData() {
  String mainImportURL = "/jsonSingle";
  JSONObject jsonImportData = fetchJSON(mainImportURL);

  // Get the "galapagusers" JSONObject
  galapagusers = jsonImportData.getJSONArray("importedUser");

  makeCA(0);
}

public void makeCA(int userNum) {
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

public void mouseReleased() {
  getImportData();
}
// Wolfram Cellular Automata
// Daniel Shiffman <http://www.shiffman.net>

// A class to manage the CA

class CA {

  int generation;  // How many generations?
  int[] ruleset;   // An array to store the ruleset, for example {0,1,1,0,1,1,0,1}
  int w = 5;
  int[][] matrix;  // Store a history of generations in 2D array, not just one

  int cols, rows, datum;


  CA(int[] r) {
    ruleset = r;
    cols = width/w;
    rows = height/w;
    matrix = new int[cols][rows];
    restart();
  }
  
  // Reset to generation 0
  public void restart() {
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        matrix[i][j] = 0;
      }
    }
    //matrix[cols/2][0] = 1;    // We arbitrarily start with just the middle cell having a state of "1"
    //int[] traits = { 10, 8, 5, 6, 2, 4, 4, 1, 4, 8, 5, 8, 9, 6, 7, 2, 10, 1, 6, 6, 8, 3, 5 };
    //int[] traits = { 10, 2, 9, 2, 10, 1, 3, 8, 4, 6, 1, 7, 5, 8, 8, 8, 4, 2, 10, 9, 6, 6, 2 };
    
    for (int i=0; i<traits.size(); i++) {
      if (traits.get(i) < 4) {
        datum = 0;
      }
      else if (traits.get(i) < 8) {
        datum = 1;
      }
      else {
        datum = 2;
      }
      matrix[i][0] = datum;
    }
    
    generation = 0;
  }


  // The process of creating the new generation
  public void generate() {

    // For every spot, determine new state by examing current state, and neighbor states
    // Ignore edges that only have one neighor
    for (int i = 0; i < cols; i++) {
      int left  = matrix[(i+cols-1)%cols][generation%rows];   // Left neighbor state
      int me    = matrix[i][generation%rows];       // Current state
      int right = matrix[(i+1)%cols][generation%rows];  // Right neighbor state
      matrix[i][(generation+1)%rows] = rules(left, me, right); // Compute next generation state based on ruleset
    }
    generation++;
  }

  // This is the easy part, just draw the cells, fill 255 for '1', fill 0 for '0'
  public void display() {
    int offset = generation%rows;

    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        int y = j - offset;
        if (y <= 0) y = rows + y;
        if (matrix[i][j] == 1) {
          fill(0);
        }
        else if (matrix[i][j] == 2) {
          fill(0,255,255); 
        }
        else {
          fill(255);
        }
        noStroke();
        rect(i*w, (y-1)*w, w, w);
      }
    }
  }

  // Implementing the Wolfram rules
  // Could be improved and made more concise, but here we can explicitly see what is going on for each case
  public int rules (int a, int b, int c) {
    String s = "" + a + b + c;
    int index = Integer.parseInt(s, 3);
    return ruleset[index];
  }

  // The CA is done if it reaches the bottom of the screen
  public boolean finished() {
    if (generation > height/w) {
      return true;
    } 
    else {
      return false;
    }
  }
}

// Learning Processing
// Daniel Shiffman
// http://www.learningprocessing.com

// Example 10-5: Object-oriented timer

class Timer {
 
  int savedTime; // When Timer started
  int totalTime; // How long Timer should last
  
  Timer(int tempTotalTime) {
    totalTime = tempTotalTime;
  }
  
  // Starting the timer
  public void start() {
    // When the timer starts it stores the current time in milliseconds.
    savedTime = millis(); 
  }
  
  // The function isFinished() returns true if 5,000 ms have passed. 
  // The work of the timer is farmed out to this method.
  public boolean isFinished() { 
    // Check how much time has passed
    int passedTime = millis()- savedTime;
    if (passedTime > totalTime) {
      return true;
    } else {
      return false;
    }
  }

}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "WolframCA_test" });
  }
}
