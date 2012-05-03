import processing.core.*; 

import java.util.Enumeration; 
import java.util.Hashtable; 
import toxi.physics2d.*; 
import toxi.physics2d.behaviors.*; 
import toxi.geom.*; 
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

public class genetic_crossing_noc_final extends PApplet {

// Ben Turner
// NYU-ITP, Intro to Computational Media
//
// documentation:
// 
// http://blog.benturner.com/2012/04/07/nature-of-code-final-proposal/
// http://blog.benturner.com/2012/03/07/nature-of-code-midterm-genetic-crossing-with-verlet-physics/
// http://blog.benturner.com/2011/12/08/genetic-crossings-icm-final-project-presentation/
// http://blog.benturner.com/2011/11/10/icm-final-project-genetic-crossing/
// http://blog.benturner.com/2011/10/16/icm-genetic-crossing-part-2/
// http://blog.benturner.com/2011/10/13/icm-genetic-crossings/

// Thanks to:
// - Prof. Dan Shiffman: all his Nature of Code work
// - Prof. John Schimmel: https://github.com/johnschimmel/DWD-Processing
// 

// TODO:
// - add external events, rituals
// - add sliders for variables

// If you add traits or variables to Person.person, you must add variables to 4 places, found by searching
// #PeopleChange (3 in main, 1 in MatingDance).








// for pulling JSON
String BASE_URL = "http://127.0.0.1:5000";

// time variables
long iterationCount = 0; // # of draw()s
int currentYear = 0; // keeps track of years since sketch start

// particles & classes, toxiclibs
VerletPhysics2D physics;
Person person[]; // init Person object array
ArrayList<VerletConstrainedSpring2D> godSpringArray;
ArrayList<VerletConstrainedSpring2D> parentSpringArray;
ArrayList<VerletMinDistanceSpring2D> parentMinDistanceSpringArray;
ArrayList<VerletConstrainedSpring2D> nationSpringArray;
ArrayList<VerletConstrainedSpring2D> religionSpringArray;
boolean updatePhysics = true;
float godGravity = 0.05f;
int godRL = 400;
int parentRL1 = 80;
int parentRL2 = 150;
int parentMinDistanceRL1 = 50;
int parentMinDistanceRL2 = 80;
float parentGravity1 = 0.1f;
float parentGravity2 = 0.3f;
int nationRL1 = 200;
int nationRL2 = 400;
float nationGravity1 = 0.005f;
float nationGravity2 = 0.01f;
int religionRL1 = 450;
int religionRL2 = 600;
float religionGravity1 = 0.001f;
float religionGravity2 = 0.008f;

Person god; // init God
Nation nation[];
Timer timer;
Religion religion[];

// set up classes
DrawFuncs drawFuncs = new DrawFuncs();
GetFuncs getFuncs = new GetFuncs();
MatingDance matingDance = new MatingDance();
Rituals rituals = new Rituals();
Evolutions evolutions = new Evolutions();
Death death = new Death();

boolean toggleNoLoop = false; // toggles space bar for stopping/starting loop

boolean funeralsRecognized = false; // if true, will destroy connection springs to the dead, purgatory is purged

// environment vars
int numCharacteristics = 31; // total characteristics per person (see Person class)
int numPeople = 5; // initialize starting # of ppl & counter for names of new people
int populationSize = 5; // size of current population
int equilibriumPopulation = 20; // so babies will be born after people die, keeps population fresh up to maxPeople
int maxPeople = 80; // so it won't go nuts; don't forget to limit and/or raise this when needed
int maxReproductiveAge = 50; // how old a woman can be and still have children
int deathAgeBegin = 60; // when death functions kick in to see if people will die
int flirter1, flirter2; // picks random flirters
int globalLabelS = 9; // text size of labels

int chemistryTolerance = 4; // max limit of attraction during flirting/chemistry
int chemistryAppearanceTolerance = 3; // max difference in peoples' appearance
int chemistryMoneyTolerance = 2; // max diff in peoples' money
int chemistryReligiosityTolerance = 2; // max diff in peoples' religiosity
int gestationRate = 5; // minimum "years" between babies
int yearLength = 20; // length of "years"

int personNodeSize = 10; // size of black circle representing each person

int numNationCharacteristics = 13;
int numNations = 5; // starting # of nations
int maxNations = 10; // so won't go out of bounds
int nationNodeSize=200; // size of nation blocs
int nationBoxSize = 10;
int nationSpacing = 200; // max distance traits can be from nation

int numReligionCharacteristics = 4;
int numReligions = 7; // starting # of nations
int maxReligions = 10; // so won't go out of bounds
int religionNodeSize=200; // size of nation blocs
int religionBoxSize = 10;
int religionSpacing = 200; // max distance traits can be from religion

int[][] colorArray = new int[numCharacteristics][3]; // need colors for all characteristics
int[] characteristicsArray = new int[numCharacteristics];

// button layouts
int buttonX = 15;
int buttonY = 30;
int buttonSizeX = 153;
int buttonSizeY = 25;
int karyogramPush = 0;
int karyogramButtonUpX = 600;
int karyogramButtonUpY = 600;
int karyogramButtonDownX = 600;
int karyogramButtonDownY = 650;
int karyogramButtonSize = 30;
int scrollingAmount = 15;
int caButtonSize = 18;
int caButtonX;
int[] caButtonY = new int[maxPeople];

// controls whether on the genetic map screen or the stats screen
boolean geneticMap = true;

public void setup() {
  size(1400, 800);
  smooth();

  // size of CA buttons on stainings screen
  caButtonX = width/2+75;
  for (int h=0;h<maxPeople;h++) {
    caButtonY[h] = 110 + 20 * h;
  }

  // Initialize the physics
  physics=new VerletPhysics2D();
  physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.0f)));

  // This is the center of the world
  Vec2D center = new Vec2D(width/2, height/2);
  // these are the worlds dimensions (50%, a vector pointing out from the center in both directions)
  Vec2D extent = new Vec2D(width/2, height/2);

  // Set the world's bounding box
  physics.setWorldBounds(Rect.fromCenterExtent(center, extent));
  //physics.setWorldBounds(new Rect(20, 20, width, height));

  // init arrays of objects
  person = new Person[maxPeople];
  nation = new Nation[maxNations];
  religion = new Religion[maxReligions];
  godSpringArray = new ArrayList<VerletConstrainedSpring2D>();
  parentSpringArray = new ArrayList<VerletConstrainedSpring2D>();
  parentMinDistanceSpringArray = new ArrayList<VerletMinDistanceSpring2D>();
  nationSpringArray = new ArrayList<VerletConstrainedSpring2D>();
  religionSpringArray = new ArrayList<VerletConstrainedSpring2D>();

  // who art in Heaven
  god = new Person(-1, 0, 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 0, 10, 0, 10, 10, 10, 10, 10, 10, 10, 10, "God", "null", -2, -2, -1, 0, -1, 0, 1, width/2, height/2);

  nation[0] = new Nation(0, "USA", 300, 300, (int)random(70, 80), (int)random(80, 100), (int)random(60, 80), (int)random(40, 60), (int)random(70, 80), 
  (int)random(70, 80), (int)random(80, 90), (int)random(70, 80), (int)random(40, 50), (int)random(70, 90), (int)random(30, 40), (int)random(70, 80), (int)random(70, 80));
  nation[1] = new Nation(1, "EU", 700, 550, (int)random(40, 50), (int)random(60, 70), (int)random(50, 60), (int)random(50, 60), (int)random(90, 100), 
  (int)random(90, 100), (int)random(80, 100), (int)random(80, 90), (int)random(80, 100), (int)random(50, 60), (int)random(70, 90), (int)random(90, 100), (int)random(90, 100));
  nation[2] = new Nation(2, "China", 300, 600, (int)random(70, 80), (int)random(40, 50), (int)random(40, 50), (int)random(30, 40), (int)random(60, 70), 
  (int)random(60, 70), (int)random(60, 70), (int)random(40, 60), (int)random(10, 20), (int)random(30, 40), (int)random(50, 60), (int)random(20, 30), (int)random(40, 60));
  nation[3] = new Nation(3, "South America", 800, 50, (int)random(20, 40), (int)random(30, 40), (int)random(30, 40), (int)random(40, 50), (int)random(60, 70), 
  (int)random(50, 60), (int)random(30, 40), (int)random(50, 60), (int)random(20, 30), (int)random(70, 80), (int)random(60, 70), (int)random(50, 60), (int)random(50, 60));
  nation[4] = new Nation(4, "Africa", 1100, 700, (int)random(10, 30), (int)random(20, 30), (int)random(20, 30), (int)random(20, 30), (int)random(20, 30), 
  (int)random(20, 30), (int)random(10, 30), (int)random(20, 30), (int)random(20, 30), (int)random(70, 80), (int)random(30, 50), (int)random(30, 50), (int)random(20, 40));

  // TODO: agnosticism, no links; atheism, a repelling force to other religions?; what else?
  religion[0] = new Religion(0, "Buddhism", 50, 400, (int)random(20, 40), (int)random(20, 30), (int)random(0, 10), (int)random(20, 30));
  religion[1] = new Religion(1, "Christianity", 600, 250, (int)random(50, 60), (int)random(80, 100), (int)random(60, 80), (int)random(60, 70));
  religion[2] = new Religion(2, "Confucianism", 100, 700, (int)random(30, 40), (int)random(80, 100), (int)random(0, 20), (int)random(20, 40));
  religion[3] = new Religion(3, "Hinduism", 300, 400, (int)random(70, 80), (int)random(80, 100), (int)random(90, 100), (int)random(60, 70));
  religion[4] = new Religion(4, "Islam", 650, 700, (int)random(70, 80), (int)random(80, 100), (int)random(60, 80), (int)random(60, 70));
  religion[5] = new Religion(5, "Judaism", 1100, 500, (int)random(90, 100), (int)random(50, 70), (int)random(60, 80), (int)random(20, 40));
  religion[6] = new Religion(6, "Taoism", 1200, 300, (int)random(20, 40), (int)random(20, 40), (int)random(10, 30), (int)random(10, 30));

  for (int i=0;i<numNations;i++) {
    physics.addParticle(nation[i]);
    nation[i].lock();
  }

  for (int i=0;i<numReligions;i++) {
    physics.addParticle(religion[i]);
    religion[i].lock();
  }

  // #PeopleChange init all person objects & nations
  for (int i=5;i<maxPeople;i++) {
    person[i] = new Person(i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "", 
    -1, -1, -1, 0, 0, 0, 0, width/2, height/2);
  }

  // #PeopleChange create some Adams and Eves and Sumerians
  person[0] = new Person(0, 0, 7, 5, 2, 6, 10, 8, 1, 6, 10, 10, 7, 6, 8, 6, 180, 67, 10, 80, 10, 10, 9, 8, 5, 2, 1, 
  7, "Albert Einstein", "male", -1, -1, 1, 0, 5, 0, 1, randomX(1), randomY(1));
  person[1] = new Person(1, 8, 5, 9, 9, 9, 2, 5, 3, 2, 2, 1, 8, 5, 2, 2, 130, 70, 4, 30, 2, 6, 7, 5, 2, 2, 10, 9, 
  "Gisele Bundchen", "female", -1, -1, 3, 0, 1, 0, 1, randomX(3), randomY(3));
  person[2] = new Person(2, 15, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(90, 300), (int)random(48, 96), (int)random(1, 10), 
  (int)random(18, 21), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), "person3", "male", -1, -1, 4, 0, 6, 0, 1, 
  randomX(4), randomY(4));
  person[3] = new Person(3, 11, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(90, 300), (int)random(48, 96), (int)random(1, 10), 
  (int)random(18, 21), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), "person4", "female", -1, -1, 0, 0, 4, 0, 0, 
  randomX(0), randomX(0));
  person[4] = new Person(4, 11, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(90, 300), (int)random(48, 96), (int)random(1, 10), 
  (int)random(18, 21), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), "person5", "female", -1, -1, 0, 0, 4, 0, 0, 
  randomX(0), randomX(0));

  // pulls in JSON from /json
  //getMainData();

  // adds God to physics world and locks God in place
  physics.addParticle(god);
  god.lock();

  // #PeopleChange init all person objects & nations
  for (int i=0;i<numPeople;i++) {
    physics.addParticle(person[i]);
    //person[i].lock();
  }

  // connect everyone to God
  for (int k=0;k<numPeople;k++) {
    godSpringArray.add(new VerletConstrainedSpring2D(god, person[k], godRL, godGravity));
    physics.addSpring(godSpringArray.get(godSpringArray.size()-1));
  }

  // connect people to their nations
  for (int i=0;i<numPeople;i++) {
    for (int j=0;j<numNations;j++) {
      nationSpringArray.add(new VerletConstrainedSpring2D(person[i], nation[j], random(nationRL1, nationRL2), random(nationGravity1, nationGravity2)));
      physics.addSpring(nationSpringArray.get(nationSpringArray.size()-1));
    }
  }

  // connect people to their religions
  for (int i=0;i<numPeople;i++) {
    for (int j=0;j<numReligions;j++) {
      religionSpringArray.add(new VerletConstrainedSpring2D(person[i], religion[j], random(religionRL1, width-person[numPeople].religiosity*50), random(religionGravity1, religionGravity2)));
      physics.addSpring(religionSpringArray.get(religionSpringArray.size()-1));
    }
  }

  // generates random color standard for all the person characteristics
  for (int i=0;i<numCharacteristics;i++) {
    for (int j=0;j<3;j++) {
      colorArray[i][j] = PApplet.parseInt(random(255));
    }
  }

  // Accessing Galapag.us characteristics
  //String resetURL = "/reset";
  //JSONObject resetResult = fetchJSON(resetURL);

  //timer = new Timer(5000);
  //timer.start();
}

// calc random x position relative to person's nationality
public float randomX(int _nation) {
  float randomX = nation[_nation].getXPos() + random(-nationSpacing, nationSpacing);
  if (randomX > width) { 
    randomX = width + (width + randomX - 30);
  }
  else if (randomX < 0) {
    randomX = 0 - randomX + 30;
  }
  return randomX;
}

// calc random y position relative to person's nationality
public float randomY(int _nation) {
  float randomY = nation[_nation].getYPos() + random(-nationSpacing, nationSpacing);
  if (randomY > height) { 
    randomY = height + (height + randomY - 30);
  }
  else if (randomY < 0) {
    randomY = 0 - randomY + 30;
  }
  return randomY;
}

public void draw() {

  if (updatePhysics == true) {
    // Update the physics world
    physics.update();
  }

  // determines speed of time, and ages people each "year" if they're alive
  if (iterationCount % yearLength == 0) {
    currentYear++;
    for (int i=0;i<numPeople;i++) {
      if (person[i].alive == true) {
        person[i].age++;
      }
    }
  }

  if (geneticMap == true) { // if user is looking at main map
    background(0);
    fill(255);
    textSize(globalLabelS);

    drawFuncs.drawNations();
    drawFuncs.drawReligions();
    drawFuncs.drawLegend();
    drawFuncs.drawPeople();
    drawFuncs.drawPopUpBoxPeople();
    drawFuncs.drawPopUpBoxNations();
    drawFuncs.drawPopUpBoxReligions();
    drawFuncs.drawInterface();

    if (mousePressed && (mouseButton == RIGHT)) {
      god.x = mouseX;
      god.y = mouseY;
    }
  }
  else { // if user is looking at stats/chromosomes
    background(0);
    stroke(0);

    drawFuncs.drawChromosomalStainings();
    drawFuncs.drawStats();
  }

  // sex & death controls
  matingDance.flirt(); // as people do
  death.deathVisit();

  // is there an attraction that may create offspring?
  if (numPeople < maxPeople && populationSize < equilibriumPopulation) { // or else person[] will go out of bounds when creating new person
    if (matingDance.chemistry(flirter1, flirter2)) {
      matingDance.sex(person[flirter1], person[flirter2]);
      // need to have a female involved here
      if (person[flirter1].gender == "female") {
        person[flirter1].lastBaby = currentYear;
      }
      else if (person[flirter2].gender == "female") {
        person[flirter2].lastBaby = currentYear;
      }
    }
    else if (matingDance.beer(flirter1, flirter2)) { // beer is social lubrication
      matingDance.sex(person[flirter1], person[flirter2]);
      if (person[flirter1].gender == "female") {
        person[flirter1].lastBaby = currentYear;
      }
      else if (person[flirter2].gender == "female") {
        person[flirter2].lastBaby = currentYear;
      }
    }
  }

  //check timer
  //  if (timer.isFinished()) {
  //    getMainData();
  //    timer.start();
  //  }

  // tracks time for year count
  iterationCount++;
}

public void keyPressed() {
  if (keyCode == 32) {
    if (toggleNoLoop == false) {
      noLoop();
      toggleNoLoop = true;
    }
    else if (toggleNoLoop == true) {
      loop();
      toggleNoLoop = false;
    }
  }
  if (key == 'u') { // stops physics engine
    if (updatePhysics == true) {
      updatePhysics = false;
    }
    else {
      updatePhysics = true;
    }
  }
  if (key == 'f') { // detaches people from their dead parents
    rituals.funeral(funeralsRecognized);
    if (funeralsRecognized == true) {
      funeralsRecognized = false;
    }
    else {
      funeralsRecognized = true;
    }
  }
}

public void mouseReleased() {

  // toggles main displays
  if ((mouseX >= buttonX) && (mouseX <= buttonX+buttonSizeX) && (mouseY >= buttonY) && (mouseY <= buttonY+buttonSizeY)) {
    if (geneticMap == true) {
      geneticMap = false;
      redraw();
    }
    else {
      geneticMap = true;
      redraw();
    }
  }

  // make font larger
  if ((mouseX >= buttonX + 155) && (mouseX <= buttonX+175) && (mouseY >= buttonY) && (mouseY <= buttonY + 25)) {
    globalLabelS -= 1;
  }

  // make font smaller
  if ((mouseX >= buttonX + 180) && (mouseX <= buttonX+200) && (mouseY >= buttonY) && (mouseY <= buttonY + 25)) {
    globalLabelS += 1;
  }

  // move karyograms down
  if (geneticMap == false && (mouseX >= karyogramButtonDownX) && (mouseX <= karyogramButtonDownX + karyogramButtonSize) && (mouseY >= karyogramButtonDownY) && (mouseY <= karyogramButtonDownY + karyogramButtonSize)) {
    karyogramPush += scrollingAmount;
  }

  // move karyograms up
  if (geneticMap == false && (mouseX >= karyogramButtonUpX) && (mouseX <= karyogramButtonUpX + karyogramButtonSize) && (mouseY >= karyogramButtonUpY) && (mouseY <= karyogramButtonUpY + karyogramButtonSize)) {
    karyogramPush -= scrollingAmount;
  }

  // make sure you set both the hash table for traits as well as direct values or no data will be returned in JSON (values will be 0)
  // send JSON to node.js
  for (int l=0; l<numPeople; l++) {
    if (geneticMap == false && (mouseX >= caButtonX) && (mouseX <= caButtonX + caButtonSize) && (mouseY >= caButtonY[l]) && (mouseY <= caButtonY[l] + caButtonSize)) {
      // prepare url to create a new circle with x and y position
      int m = l + 1;
      String importUserURL = "/import?namePerson=person" + m + "&strength=" + person[l].strength + "&intelligence=" + person[l].intelligence +
        "&wisdom=" + person[l].wisdom + "&charisma=" + person[l].charisma + "&stamina=" + person[l].stamina +
        "&wit=" + person[l].wit + "&humor=" + person[l].humor + "&education=" + person[l].education + 
        "&creativity=" + person[l].creativity + "&responsibility=" + person[l].responsibility + "&discipline=" + person[l].discipline + 
        "&honesty=" + person[l].honesty + "&religiosity=" + person[l].religiosity + "&entrepreneurialism=" + person[l].entrepreneurialism + 
        "&appearance=" + person[l].appearance + "&money=" + person[l].money + "&gracefulness=" + person[l].gracefulness + 
        "&stress=" + person[l].stress + "&health=" + person[l].health + "&luck=" + person[l].luck +
        "&talent_math=" + person[l].talentMath + "&talent_art=" + person[l].talentArt + "&talent_sports=" + person[l].talentSports;
      JSONObject importData = fetchJSON(importUserURL);
    }
  }
}

public JSONObject fetchJSON(String URL_PATH) {

  // Get the JSON formatted response
  String response = loadStrings(BASE_URL + URL_PATH)[0];

  // Make sure we got a response.
  if (response != null) {
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

public void getMainData() {

  String mainURL = "/json";
  JSONObject jsonData = fetchJSON(mainURL);

  if (jsonData != null) {
    // Get the "galapagusers" JSONObject
    JSONArray galapagusers = jsonData.getJSONArray("galapagusers");

    // Loop through and print out each galapaguser
    for (int i=0; i<galapagusers.length(); i++) {
      println(galapagusers.opt(i)); // .opt(indexNum) will retrieve the current JSONArray element
      JSONObject fetchedGalapaguser = (JSONObject)galapagusers.opt(i);
      Person fillerParent1 = new Person(-1, 0, 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 0, 10, 0, 10, 10, 10, 10, 10, 10, 10, 10, "God", "null", -2, -2, -1, 0, -1, 0, 1, width/2, height/2);
      Person fillerParent2 = new Person(-1, 0, 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 0, 10, 0, 10, 10, 10, 10, 10, 10, 10, 10, "God", "null", -2, -2, -1, 0, -1, 0, 1, width/2, height/2);
      int currentPerson = numPeople;
      matingDance.sex(fillerParent1, fillerParent2);

      // TODO: clean up this hack; dupe of code in MatingDance
      person[currentPerson].uniqueID = currentPerson;
      person[currentPerson].trait.put("strength", fetchedGalapaguser.optInt("strength"));
      person[currentPerson].trait.put("intelligence", fetchedGalapaguser.optInt("intelligence"));
      person[currentPerson].trait.put("wisdom", fetchedGalapaguser.optInt("wisdom"));
      person[currentPerson].trait.put("charisma", fetchedGalapaguser.optInt("charisma"));
      person[currentPerson].trait.put("stamina", fetchedGalapaguser.optInt("stamina"));
      person[currentPerson].trait.put("wit", fetchedGalapaguser.optInt("wit"));
      person[currentPerson].trait.put("humor", fetchedGalapaguser.optInt("humor"));
      person[currentPerson].trait.put("education", fetchedGalapaguser.optInt("education"));
      person[currentPerson].trait.put("creativity", fetchedGalapaguser.optInt("creativity"));
      person[currentPerson].trait.put("responsibility", fetchedGalapaguser.optInt("responsibility"));
      person[currentPerson].trait.put("discipline", fetchedGalapaguser.optInt("discipline"));
      person[currentPerson].trait.put("honesty", fetchedGalapaguser.optInt("honesty"));
      person[currentPerson].trait.put("religiosity", fetchedGalapaguser.optInt("religiosity"));
      person[currentPerson].trait.put("entrepreneurialism", fetchedGalapaguser.optInt("entrepreneurialism"));
      person[currentPerson].trait.put("appearance", fetchedGalapaguser.optInt("appearance"));
      person[currentPerson].trait.put("money", fetchedGalapaguser.optInt("money"));
      person[currentPerson].trait.put("gracefulness", fetchedGalapaguser.optInt("gracefulness"));
      person[currentPerson].trait.put("stress", fetchedGalapaguser.optInt("stress"));
      person[currentPerson].trait.put("health", fetchedGalapaguser.optInt("health"));
      person[currentPerson].trait.put("luck", fetchedGalapaguser.optInt("luck"));
      person[currentPerson].trait.put("math talent", fetchedGalapaguser.optInt("talent_math"));
      person[currentPerson].trait.put("art talent", fetchedGalapaguser.optInt("talent_art"));
      person[currentPerson].trait.put("sports talent", fetchedGalapaguser.optInt("talent_sports"));

      person[currentPerson].namePerson = fetchedGalapaguser.optString("name");
      person[currentPerson].strength = fetchedGalapaguser.optInt("strength");
      person[currentPerson].intelligence = fetchedGalapaguser.optInt("intelligence");
      person[currentPerson].wisdom = fetchedGalapaguser.optInt("wisdom");
      person[currentPerson].charisma = fetchedGalapaguser.optInt("charisma");
      person[currentPerson].stamina = fetchedGalapaguser.optInt("stamina");
      person[currentPerson].wit = fetchedGalapaguser.optInt("wit");
      person[currentPerson].humor = fetchedGalapaguser.optInt("humor");
      person[currentPerson].education = fetchedGalapaguser.optInt("education");
      person[currentPerson].creativity = fetchedGalapaguser.optInt("creativity");
      person[currentPerson].responsibility = fetchedGalapaguser.optInt("responsibility");
      person[currentPerson].discipline = fetchedGalapaguser.optInt("discipline");
      person[currentPerson].honesty = fetchedGalapaguser.optInt("honesty");
      person[currentPerson].religiosity = fetchedGalapaguser.optInt("religiosity");
      person[currentPerson].entrepreneurialism = fetchedGalapaguser.optInt("entrepreneurialism");
      person[currentPerson].appearance = fetchedGalapaguser.optInt("appearance");
      person[currentPerson].money = fetchedGalapaguser.optInt("money");
      person[currentPerson].gracefulness = fetchedGalapaguser.optInt("gracefulness");
      person[currentPerson].stress = fetchedGalapaguser.optInt("stress");
      person[currentPerson].health = fetchedGalapaguser.optInt("health");
      person[currentPerson].luck = fetchedGalapaguser.optInt("luck");
      person[currentPerson].talentMath = fetchedGalapaguser.optInt("talent_math");
      person[currentPerson].talentArt = fetchedGalapaguser.optInt("talent_art");
      person[currentPerson].talentSports = fetchedGalapaguser.optInt("talent_sports");
    }
  }
}

public class Death {
  Death() {
  }

  // checks if it's time to die if someone's alive (important so you don't also do already dead people)
  public void deathVisit() {
    for (int i=0;i<maxPeople;i++) {
      if (person[i].age > 115 && person[i].alive == true) {
        killPerson(i);
      }
      else {
        if (person[i].age > deathAgeBegin && person[i].alive == true) {
          int deathCalling = person[i].age + (int)(Float.parseFloat(person[i].trait.get("stress").toString()));
          int deathAversion = (int)(Float.parseFloat(person[i].trait.get("health").toString())) + (int)(Float.parseFloat(person[i].trait.get("luck").toString())) - (int)((person[i].age-77) / 2);
          int randomDeathRoll = (int)random(0, 14.99f);
          if (deathAversion < randomDeathRoll) {
            killPerson(i);
          }
        }
      }
    }
  }

  public void killPerson(int _personID) {
    person[_personID].alive = false;
    person[_personID].display(1);
    //person[_personID].lock();
    populationSize--;
  }
}

public class DrawFuncs {

  DrawFuncs() {
  }

  public void drawPeople() {

    // only draw actual people, not empty objects
    for (int i=0;i<numPeople;i++) {
      int strength = (int)Float.parseFloat(person[i].trait.get("strength").toString());
      if (strength != 0) {
        if (person[i].alive == false && funeralsRecognized == true) {
        }
        else {
          person[i].display(50);
          drawFamilyLines(i, 25);
          drawFuncs.drawHappiness(i);
        }
        /* person[i].x = person[i].x + calcCultureMoveX(i, person[i].x);
         int finalY = calcCultureMoveY(i, person[i].y);
         int speedY = ceil(abs(person[i].y - finalY) / 20);
         if (person[i].y < finalY) person[i].y = person[i].y + speedY;
         */
      }
    }
    // show God
    god.display(50);
  }

  public void drawFamilyLines(int i, int lineOpacity) { // draw familial connection lines

    stroke(255, 255, 255, lineOpacity);
    line(god.x, god.y, person[i].x, person[i].y);

    if (person[i].parent1 != -1) {
      stroke(0, 0, 255, lineOpacity);
      int midPointParent1X = round((person[person[i].parent1].x + person[i].x) / 2);
      int midPointParent1Y = round((person[person[i].parent1].y + person[i].y) / 2);
      int midPointParent2X = round((person[person[i].parent2].x + person[i].x) / 2);
      int midPointParent2Y = round((person[person[i].parent2].y + person[i].y) / 2);
      line(person[person[i].parent1].x, person[person[i].parent1].y, person[i].x, person[i].y);
      line(person[person[i].parent2].x, person[person[i].parent2].y, person[i].x, person[i].y);
      stroke(0, 255, 0, lineOpacity);
      fill(0, 0, 255, 255);
      text(Integer.toString(person[i].parent1RL), midPointParent1X, midPointParent1Y);
      text(Integer.toString(person[i].parent2RL), midPointParent2X, midPointParent2Y);
      line(person[person[i].parent1].x, person[person[i].parent1].y, person[person[i].parent2].x, person[person[i].parent2].y);
    }

    // nation connection lines
    stroke(255, 255, 0, lineOpacity);
    line(person[i].x, person[i].y, nation[person[i].nationality].x, nation[person[i].nationality].y);
    fill(255, 255, 0, 255);
    int midPointNationX = round((nation[person[i].nationality].x + person[i].x) / 2);
    int midPointNationY = round((nation[person[i].nationality].y + person[i].y) / 2);
    text(Integer.toString(person[i].nationRL), midPointNationX, midPointNationY);

    // religion connection lines
    stroke(3, 255, 181, lineOpacity);
    line(person[i].x, person[i].y, religion[person[i].religion].x, religion[person[i].religion].y);
    fill(0, 255, 255, 255);
    int midPointReligionX = round((religion[person[i].religion].x + person[i].x) / 2);
    int midPointReligionY = round((religion[person[i].religion].y + person[i].y) / 2);
    text(Integer.toString(person[i].religionRL), midPointReligionX, midPointReligionY);
    stroke(0);
  }

  public void drawNations() {
    for (int i=0;i<numNations;i++) {
      int security = (int)Float.parseFloat(nation[i].trait.get("security").toString());
      if (security != 0) {
        nation[i].display(10);
      }
    }
  }

  public void drawReligions() {
    for (int i=0;i<numReligions;i++) {
      int commercial = (int)Float.parseFloat(religion[i].trait.get("commercial").toString());
      if (commercial != 0) {
        religion[i].display(10);
      }
    }
  }

  public void drawLegend() {
    textSize(globalLabelS);
    int i = 0; // iterator for which characteristic to print for the legend
    int j = 30; // distance between each item in the legend
    stroke(0);

    // loads circle diameters from hashtable, iterates thru all characteristics
    // person[0] arbitrary usage for pulling out characteristics
    for (Enumeration e = person[1].trait.keys() ; e.hasMoreElements() ;) {
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
      rect(width-100, j, 5, 5);
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
      text(e.nextElement().toString(), width-90, j+6);
      i++;
      j = j + 15;
    }
  }

  // when you mouseover a node, some basic data is displayed
  public void drawPopUpBoxPeople() {
    String actualTrait = "";
    String[] mbti = new String[2];

    for (int i=0;i<numPeople;i++) {
      int strength = (int)Float.parseFloat(person[i].trait.get("strength").toString());
      if (strength != 0) {
        if (mouseX >= person[i].x-(PApplet.parseInt(personNodeSize/2)) && mouseX <= person[i].x+(PApplet.parseInt(personNodeSize/2)) && mouseY >= person[i].y-(PApplet.parseInt(personNodeSize/2)) && mouseY <= person[i].y+(PApplet.parseInt(personNodeSize/2))) {
          drawFamilyLines(i, 255);
          person[i].display(255);
          if (mousePressed) {
            textSize(10);
            int drawBoxX = mouseX;
            int drawBoxY = mouseY;
            int drawBoxWidth = 180;
            int drawBoxHeight = 400;
            if ((drawBoxY + drawBoxHeight) > height) {
              drawBoxY = drawBoxY - 15 - abs(height-(drawBoxY + drawBoxHeight));
            }
            fill(255);
            stroke(0);

            rect(drawBoxX+35, drawBoxY+15, drawBoxWidth, drawBoxHeight);
            fill(0);
            int pHeight = round(person[i].pHeight * 5 * person[i].age / 100);
            int pWeight = round(person[i].pWeight * 2 * person[i].age / 100);
            if (pHeight > person[i].pHeight) { 
              pHeight = person[i].pHeight;
            }
            if (pWeight > person[i].pWeight) { 
              pWeight = person[i].pWeight;
            }
            int pFeet = round(pHeight / 12);
            int pInches = pHeight % 12;
            mbti = getFuncs.getMBTI(person[i].mbti);
            text(person[i].namePerson + " (" + person[i].gender + ", " + person[i].age + "yo)\n" +
              getFuncs.getNationality(person[i].nationality) + ", " + getFuncs.getReligion(person[i].religion) + "\n" + mbti[0] + ", " + mbti[1] + "\n" + pWeight +
              "lbs, " + pFeet + "'" + pInches + "\"", drawBoxX+45, drawBoxY+30);
            int j = 0;
            int k = 0; // distance between each item in the legend

            // draws popup window for values from each person node
            Object characteristic;
            for (Enumeration e = person[1].trait.keys() ; e.hasMoreElements() ;) {
              characteristic = e.nextElement();
              fill(colorArray[j][0], colorArray[j][1], colorArray[j][2]);
              text(characteristic.toString(), drawBoxX+75, drawBoxY+95+k);
              String _characteristic = person[i].trait.get(characteristic).toString();
              int potentialTrait = PApplet.parseInt(_characteristic);
              if ((characteristic.toString() == "strength") && (characteristic.toString() == "wisdom") && (characteristic.toString() == "education")) {
                int _actualTrait = ceil(potentialTrait * person[i].age * 2 / 100);
                if (potentialTrait < _actualTrait) {
                  actualTrait = Integer.toString(potentialTrait);
                }
              }
              else {
                actualTrait = Integer.toString(potentialTrait);
              }
              text(actualTrait, drawBoxX+45, drawBoxY+95+k);
              j++;
              k += 15;
            }
          }
        }
      }
    }
  }

  // when you mouseover a node, some basic data is displayed
  public void drawPopUpBoxNations() {
    for (int i=0;i<numNations;i++) {
      int strength = (int)Float.parseFloat(nation[i].trait.get("security").toString());
      if (strength != 0) {
        if ((mouseX >= nation[i].x-PApplet.parseInt(nationBoxSize/2)) && (mouseX <= nation[i].x+PApplet.parseInt(nationBoxSize/2)) && (mouseY >= nation[i].y-PApplet.parseInt(nationBoxSize/2)) && (mouseY <= nation[i].y+PApplet.parseInt(nationBoxSize/2))) {
          nation[i].display(150);
          textSize(14);
          int drawBoxX = mouseX;
          int drawBoxY = mouseY;
          int drawBoxWidth = 180;
          int drawBoxHeight = 250;
          if ((drawBoxY + drawBoxHeight) > height) {
            drawBoxY = drawBoxY - 15 - abs(height-(drawBoxY + drawBoxHeight));
          }
          fill(255);
          stroke(0);
          rect(drawBoxX+15, drawBoxY+15, drawBoxWidth, drawBoxHeight);
          fill(0);
          text(nation[i].nameNation, drawBoxX+30, drawBoxY+40);
          int j = 0;
          int k = 0; // distance between each item in the legend

          // draws popup window for values from each person node
          Object characteristic;
          int z = 0;
          for (Enumeration e = nation[1].trait.keys() ; e.hasMoreElements() ;) {
            characteristic = e.nextElement();
            fill(0);
            text(characteristic.toString(), drawBoxX+45, drawBoxY+75+k);
            fill(nation[i].colorBaseArraySquares[z][0], nation[i].colorBaseArraySquares[z][1], nation[i].colorBaseArraySquares[z][2]);
            text(nation[i].trait.get(characteristic).toString(), drawBoxX+20, drawBoxY+75+k);
            j++;
            z++;
            k += 15;
          }
        }
      }
    }
  }

  // when you mouseover a node, some basic data is displayed
  public void drawPopUpBoxReligions() {
    for (int i=0;i<numReligions;i++) {
      int commercial = (int)Float.parseFloat(religion[i].trait.get("commercial").toString());
      if (commercial != 0) {
        if ((mouseX >= religion[i].x) && (mouseX <= religion[i].x+religionBoxSize) && (mouseY >= religion[i].y) && (mouseY <= religion[i].y+religionBoxSize)) {
          religion[i].display(150);
          textSize(14);
          int drawBoxX = mouseX;
          int drawBoxY = mouseY;
          int drawBoxWidth = 150;
          int drawBoxHeight = 150;
          if ((drawBoxY + drawBoxHeight) > height) {
            drawBoxY = drawBoxY + 15 - abs(height-(drawBoxY + drawBoxHeight));
          }
          fill(255);
          stroke(0);
          rect(drawBoxX+15, drawBoxY+15, drawBoxWidth, drawBoxHeight);
          fill(0);
          text(religion[i].nameReligion, drawBoxX+30, drawBoxY+40);
          int j = 0;
          int k = 0; // distance between each item in the legend

          // draws popup window for values from each person node
          Object characteristic;
          int z = 0;
          for (Enumeration e = religion[1].trait.keys() ; e.hasMoreElements() ;) {
            characteristic = e.nextElement();
            fill(0);
            text(characteristic.toString(), drawBoxX+45, drawBoxY+75+k);
            fill(religion[i].colorBaseArraySquares[z][0], religion[i].colorBaseArraySquares[z][1], religion[i].colorBaseArraySquares[z][2]);
            text(religion[i].trait.get(characteristic).toString(), drawBoxX+20, drawBoxY+75+k);
            j++;
            z++;
            k += 15;
          }
        }
      }
    }
  }

  public void drawInterface() {
    fill(255);
    textSize(globalLabelS);
    text("'u' stops physics, 'space' stops sketch, blue = parent -> child, green = parents, yellow = nationality, turquoise = religion", 15, 15);

    // buttons
    noStroke();

    // chromosomal stainings/time universe
    if ((mouseX >= buttonX) && (mouseX <= buttonX+buttonSizeX) && (mouseY >= buttonY) && (mouseY <= buttonY+buttonSizeY)) {
      fill(0xffFFD95D);
    }
    else {
      fill(0xffFFC400);
    }
    rect(buttonX, buttonY, buttonSizeX, buttonSizeY);

    textSize(10);
    fill(0);
    text("stats & chromosomal stainings", buttonX+4, buttonY+15);

    // make font bigger
    if ((mouseX >= buttonX + 155) && (mouseX <= buttonX+175) && (mouseY >= buttonY) && (mouseY <= buttonY + 25)) {
      fill(0xffFFD95D);
    }
    else {
      fill(0xffFFC400);
    }
    rect(buttonX + 155, buttonY, 20, 25);

    textSize(10);
    fill(0);
    text("f", buttonX+163, buttonY+15);

    // make font smaller
    if ((mouseX >= buttonX + 180) && (mouseX <= buttonX+200) && (mouseY >= buttonY) && (mouseY <= buttonY + 25)) {
      fill(0xffFFD95D);
    }
    else {
      fill(0xffFFC400);
    }
    rect(buttonX + 180, buttonY, 20, 25);

    textSize(10);
    fill(0);
    text("F", buttonX+188, buttonY+15);
  }

  // loads chromosomal stainings view
  public void drawChromosomalStainings() {
    noStroke();
    if ((mouseX >= buttonX) && (mouseX <= buttonX+buttonSizeX) && (mouseY >= buttonY) && (mouseY <= buttonY+buttonSizeY)) {
      fill(0xffFFD95D);
    }
    else {
      fill(0xffFFC400);
    }
    rect(buttonX, buttonY, buttonSizeX, buttonSizeY);

    textSize(10);
    fill(0);
    text("view genetic/time map", buttonX+8, buttonY+15);
    int i = 0; // iterator for which characteristic to print for the legend
    int j = 20; // distance between each item in the legend
    for (int h=0;h<numPeople;h++) {
      for (Enumeration e = person[h].trait.keys() ; e.hasMoreElements() ;) {
        Object characteristic = e.nextElement();
        Object diameterCirc = person[h].trait.get(characteristic);
        float _diameter = Float.parseFloat(diameterCirc.toString());
        if (person[h].alive) {
          fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
        }
        else {
          fill(100, 150);
        }
        ellipse(width/2 + 120 + i * 20, karyogramPush + 120 + j * h, _diameter * 3, _diameter * 3);
        fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
        i++;
      }
      textSize(14);
      fill(255);
      textAlign(RIGHT);
      text(person[h].namePerson, width/2+50, karyogramPush + 125 + j * h);
      textAlign(BASELINE);
      fill(255);
      textSize(10);
      rect(caButtonX, karyogramPush + 110 + j * h, caButtonSize, caButtonSize);
      fill(0);
      text("CA", width/2+77, karyogramPush + 123 + j * h);
      i = 0;
    }
    int k = 15;
    int l = 0;
    pushMatrix();
    translate(width/2+120, height/2);
    textSize(9);
    rotate(-HALF_PI);
    Object characteristic;
    for (Enumeration e = person[1].trait.keys() ; e.hasMoreElements() ;) {
      characteristic = e.nextElement();
      fill(255);
      text(characteristic.toString(), 300, 4+l * j);
      l++;
    }
    popMatrix();

    // push karyograms down
    if ((mouseX >= karyogramButtonDownX) && (mouseX <= karyogramButtonDownX + karyogramButtonSize) && (mouseY >= karyogramButtonDownY) && (mouseY <= karyogramButtonDownY + karyogramButtonSize)) {
      fill(0xffFFD95D);
    }
    else {
      fill(0xffFFC400);
    }
    rect(karyogramButtonUpX, karyogramButtonUpY, karyogramButtonSize, karyogramButtonSize);

    textSize(10);
    fill(0);
    text("up", karyogramButtonUpX+8, karyogramButtonUpY+17);

    // push karyograms up
    if ((mouseX >= karyogramButtonUpX) && (mouseX <= karyogramButtonUpX + karyogramButtonSize) && (mouseY >= karyogramButtonUpY) && (mouseY <= karyogramButtonUpY + karyogramButtonSize)) {
      fill(0xffFFD95D);
    }
    else {
      fill(0xffFFC400);
    }
    rect(karyogramButtonDownX, karyogramButtonDownY, karyogramButtonSize, karyogramButtonSize);

    textSize(10);
    fill(0);
    text("down", karyogramButtonDownX+3, karyogramButtonDownY+17);
  }

  public void drawStats() {
    fill(255);

    int avgAgeTotal = 0;
    int mostChildren = 0;
    int mostChildrenId = 0;
    int livePeople = 0;
    int[] mbti = new int[16];
    int[] personCharsRawTotal = new int[numPeople];

    for (int i=0;i<mbti.length;i++) {
      mbti[i] = 0;
    }
    for (int i=0;i<numPeople;i++) {
      if (person[i].alive == true) {
        avgAgeTotal += person[i].age;
        livePeople++;
      }
      mbti[person[i].mbti]++;

      personCharsRawTotal[i] = evolutions.personCharsRawTotal(i);

      if (person[i].children > mostChildren) {
        mostChildren = person[i].children;
        mostChildrenId = i;
      }
    }

    // avg age  
    int avgAge;
    if (livePeople > 0) {
      avgAge = PApplet.parseInt(avgAgeTotal / livePeople);
    }
    else {
      avgAge = 0;
    }
    text("average age: " + avgAge, 10, 80);

    // most offspring
    text("most children: " + person[mostChildrenId].namePerson + ", " + mostChildren, 10, 100);

    // jack-of-all-trades winner
    int highestCharsTotal = 0;
    int highestCharsId = -1;
    for (int i=1;i<numPeople;i++) {
      if (personCharsRawTotal[i] > highestCharsTotal) {
        highestCharsId = i;
        highestCharsTotal = personCharsRawTotal[i];
      }
    }
    int personCharsAvg = PApplet.parseInt(personCharsRawTotal[highestCharsId] / numCharacteristics);
    text("jack of all trades award: ", 10, 120);
    text(" " + person[highestCharsId].namePerson + " (" + personCharsRawTotal[highestCharsId] + ", avg: " + personCharsAvg + ")", 10, 135);

    // myers-briggs count
    text("personality types:", 10, 155);
    for (int i=0;i<mbti.length;i++) {
      text(" " + getFuncs.getMBTI(i)[0] + " (" + getFuncs.getMBTI(i)[1] + "): " + mbti[i], 10, 170 + 15 * i);
    }

    int deadPeople = 0;
    text("global population: " + populationSize + " (equilibrium: " + equilibriumPopulation + ")", 10, 420);
    for (int i=0;i<maxPeople;i++) {
      if (person[i].alive == false) {
        deadPeople++;
      }
    }
    text("dead people: " + deadPeople, 10, 435);

    // country count

    int populationNation[] = new int[numNations];
    int yLine = 80;
    int personCharsNationTotal = 0;
    int personCharsNationRaw = 0;
    int personCharsRealTotal = 0;
    int personCharsIdealTotal = 0;
    float ratioPotential = 0.0f;

    for (int j=0;j<numPeople;j++) {
      if (person[j].alive == true) {
        populationNation[person[j].nationality]++;
      }
    }

    for (int i=0;i<numNations;i++) {
      personCharsNationRaw = 0;
      personCharsRealTotal = 0;
      personCharsIdealTotal = 0;
      for (int k=0;k<numPeople;k++) {
        if (person[k].nationality == i && person[k].alive == true) {
          personCharsNationRaw += evolutions.personCharsRawTotal(k);
          personCharsRealTotal += evolutions.actualTotal(k);
          personCharsIdealTotal += evolutions.idealTotal(k);
        }
      }
      if (personCharsRealTotal == 0) { 
        personCharsRealTotal = 1;
      }
      if (personCharsIdealTotal == 0) { 
        personCharsIdealTotal = 1;
      }
      ratioPotential = PApplet.parseFloat(personCharsRealTotal) / PApplet.parseFloat(personCharsIdealTotal);

      text(nation[i].nameNation, 200, yLine);
      yLine += 15;
      text(" population: " + populationNation[i], 200, yLine);
      yLine += 15;
      text(" raw well-being: " + personCharsNationRaw, 200, yLine);
      yLine += 15;
      text(" actual/potential well-being ratio: " + nf(ratioPotential, 0, 2), 200, yLine);
      yLine += 20;
    }

    // religion count
    int populationReligion[] = new int[numReligions];

    for (int j=0;j<numPeople;j++) {
      if (person[j].alive == true) {
        populationReligion[person[j].religion]++;
      }
    }

    for (int i=0;i<numReligions;i++) {
      text(religion[i].nameReligion + " population: " + populationReligion[i], 200, yLine);
      yLine += 15;
    }
  }

  // draws happiness & color under person's text in map view
  public void drawHappiness(int i) {
    int happiness = evolutions.happiness(i);
    String happinessText;
    int happinessColor = 0xffFFFFFF;
    if (happiness < 20) {
      happinessColor = 0xffFF0000;
      happinessText = "very unhappy";
    }
    else if (happiness < 40) {
      happinessColor = 0xffFF7F24;
      happinessText = "unhappy";
    }
    else if (happiness < 60) {
      happinessColor = 0xffFFE303;
      happinessText = "content";
    }
    else {
      happinessColor = 0xff99CC32;
      happinessText = "happy";
    }
    fill(happinessColor, 200);
    text(happinessText, person[i].x+15, person[i].y+34);
  }

  // didn't implement but maybe will be useful

  /*
int calcCultureMoveX(int personID, int personX) {
   int totalMoveX = 0;
   float _influence;
   for (int i=0;i<numNations;i++) {
   _influence = Float.parseFloat(person[personID].trait.get("intelligence").toString()) - Float.parseFloat(nation[i].trait.get("innovation").toString());
   int _absInfluence = abs(int(_influence));
   if (_absInfluence <= 10) {
   totalMoveX = nation[i].x - personX;
   totalMoveX = ceil(totalMoveX / 20);
   }
   }
   return totalMoveX;
   }
   
   int calcCultureMoveY(int personID, int personY) {
   int totalMoveY = 0;
   float _influence;
   for (int i=0;i<numNations;i++) {
   _influence = Float.parseFloat(person[personID].trait.get("intelligence").toString()) - ceil(Float.parseFloat(nation[i].trait.get("innovation").toString()) / 10);
   int _absInfluence = abs(int(_influence));
   System.out.println(_absInfluence);
   if (_absInfluence <= 1) {
   totalMoveY = (nation[i].y + personY) / 2;
   }
   }
   return totalMoveY;
   }
   */
}

public class Evolutions {

  Evolutions() {
  }

  // basic stats
  public int personCharsRawTotal(int i) {
    int personCharsTotal = 0;
    for (Enumeration e = person[i].trait.keys() ; e.hasMoreElements() ;) {
      Object characteristic = e.nextElement();
      Object charVal = person[i].trait.get(characteristic);
      personCharsTotal += PApplet.parseInt(Float.parseFloat(charVal.toString()));
    }
    return personCharsTotal;
  }
  
  // calc a person's happiness
  public int happiness(int i) {
    int happiness = person[i].health + person[i].money + person[i].stress + person[i].creativity + person[i].religiosity +
    round(nation[person[i].nationality].standardOfLiving / 10) + round(nation[person[i].nationality].pollution / 10) +
    round(nation[person[i].nationality].security / 10) + round(nation[person[i].nationality].crime / 10) + person[i].employed * 10;
    return happiness;
  }

  // what stats a person actually has
  public int actualTotal(int i) {
    int pCAT = 0;
    int strength = 0, intelligence = 0, education = 0, creativity = 0, religiosity = 0, entrepreneurialism = 0, money = 0, stress = 0, health = 0;

    strength = round((person[i].strength * 10 + PApplet.parseInt((nation[person[i].nationality].pollution) * (nation[person[i].nationality].nutrition) * person[i].health)) / 1000);
    intelligence = person[i].intelligence * 10 + PApplet.parseInt((person[i].education) * (nation[person[i].nationality].education));
    education = round((person[i].education * 10 + PApplet.parseInt((nation[person[i].nationality].education) * (nation[person[i].nationality].standardOfLiving) * (nation[person[i].nationality].crime))) / 10000);
    creativity = person[i].creativity * 10 + PApplet.parseInt((nation[person[i].nationality].innovation) * (person[i].education * 5));
    religiosity = person[i].religiosity * 10 + PApplet.parseInt((religion[person[i].religion].morality * 3) * (nation[person[i].nationality].education));
    entrepreneurialism = round((person[i].entrepreneurialism * 10 + PApplet.parseInt((nation[person[i].nationality].innovation) * (nation[person[i].nationality].jobOpportunity) *
      (nation[person[i].nationality].immigrationPolicy))) / 10000);
    money = round((person[i].money * 10 + PApplet.parseInt((nation[person[i].nationality].jobOpportunity * 5) * (person[i].education * 5) * (person[i].intelligence * 10) * (person[i].creativity * 15)
      * (person[i].luck * 20) * (religion[person[i].religion].commercial * 2))) / 1000000000);
    stress = round((person[i].stress * 10 + PApplet.parseInt((person[i].health * 5) * (nation[person[i].nationality].security) * (nation[person[i].nationality].politicalFreedom) *
      (nation[person[i].nationality].nutrition * 2) * (religion[person[i].religion].morality * 2))) / 100000000);
    health = round((person[i].health * 10 + PApplet.parseInt((nation[person[i].nationality].pollution * 2) * (nation[person[i].nationality].standardOfLiving * 10))) / 1000);

    pCAT += strength + intelligence + education + creativity + religiosity + entrepreneurialism + money + stress + health;
    return pCAT;
  }

  // what stats a person could potentially have in ideal environment
  public int idealTotal(int i) {
    int pCAT = 0;
    int strength = 0, intelligence = 0, education = 0, creativity = 0, religiosity = 0, entrepreneurialism = 0, money = 0, stress = 0, health = 0;

    strength = round((100 + 100 * 100 * 10) / 1000);
    intelligence = 100 + 10 * 100;
    education = round((100 + 100 * 100 * 100) / 10000);
    creativity = 100 + 100 * 50;
    religiosity = 100 + 300 * 100;
    entrepreneurialism = round((100 + 100 * 100 * 100) / 10000);
    money = round((100 + 500 * 50 * 100 * 150 * 200 * 200) / 1000000000);
    stress = round((100 + 50 * 100 * 100 * 200 * 200) / 100000000);
    health = round((100 + 200 * 1000) / 1000);

    pCAT += strength + intelligence + education + creativity + religiosity + entrepreneurialism + money + stress + health;
    return pCAT;
  }
  
}

public class GetFuncs {

  GetFuncs() {
  }

  public String getNationality(int _getNationality) {
    String _nationality;
    switch (_getNationality) {
    case 0:
      _nationality = "USA";
      break;
    case 1:
      _nationality = "EU";
      break;
    case 2:
      _nationality = "China";
      break;
    case 3:
      _nationality = "South America";
      break;
    case 4:
      _nationality = "Africa";
      break;
    default:
      _nationality = "USA";
    }
    return _nationality;
  }

  public String getReligion(int _getReligion) {
    String _religion;
    switch (_getReligion) {
    case 0:
      _religion = "Buddhism";
      break;
    case 1:
      _religion = "Christianity";
      break;
    case 2:
      _religion = "Confucianism";
      break;
    case 3:
      _religion = "Hinduism";
      break;
    case 4:
      _religion = "Islam";
      break;
    case 5:
      _religion = "Judaism";
      break;
    case 6:
      _religion = "Taoism";
      break;
    default:
      _religion = "Islam";
    }
    return _religion;
  }

  public String[] getMBTI(int _getMBTI) {
    String[] _mbti = new String[2];
    switch (_getMBTI) {
    case 0:
      _mbti[0] = "INTP";
      _mbti[1] = "Architect";
      break;
    case 1:
      _mbti[0] = "INTJ";
      _mbti[1] = "Mastermind";
      break;
    case 2:
      _mbti[0] = "ENTP";
      _mbti[1] = "Inventor";
      break;
    case 3:
      _mbti[0] = "ENTJ";
      _mbti[1] = "Field Marshall";
      break;
    case 4:
      _mbti[0] = "INFP";
      _mbti[1] = "Healer";
      break;
    case 5:
      _mbti[0] = "INFJ";
      _mbti[1] = "Counselor";
      break;
    case 6:
      _mbti[0] = "ENFP";
      _mbti[1] = "Champion";
      break;
    case 7:
      _mbti[0] = "ENFJ";
      _mbti[1] = "Teacher";
      break;
    case 8:
      _mbti[0] = "ESFP";
      _mbti[1] = "Performer";
      break;
    case 9:
      _mbti[0] = "ESTP";
      _mbti[1] = "Promoter";
      break;
    case 10:
      _mbti[0] = "ISFP";
      _mbti[1] = "Composer";
      break;
    case 11:
      _mbti[0] = "ISTP";
      _mbti[1] = "Crafter";
      break;
    case 12:
      _mbti[0] = "ESFJ";
      _mbti[1] = "Provider";
      break;
    case 13:
      _mbti[0] = "ESTJ";
      _mbti[1] = "Supervisor";
      break;
    case 14:
      _mbti[0] = "ISFJ";
      _mbti[1] = "Protector";
      break;
    case 15:
      _mbti[0] = "ISTJ";
      _mbti[1] = "Inspector";
      break;
    }
    return _mbti;
  }
}

public class MatingDance {

  MatingDance() {
  }

  // gets two unique male-female pairs to test compatibility
  public void flirt() {
    boolean twoUniques = false;
    int randPpl = numPeople - 1;
    flirter1 = (int)random(randPpl);
    while (twoUniques == false) {
      flirter2 = (int)random(randPpl);
      if (flirter1 != flirter2) {
        twoUniques = true;
      }
    }
  }

  // takes two results from flirt() and sees if they pass certain reproduction tests
  // e.g. male-female pair? similar appearance level? same level of money?
  // then returns boolean saying whether sex() will occur
  public boolean chemistry(int _flirter1, int _flirter2) {
    boolean procreate = false;
    int randomTest = (int)random(10);
    if (randomTest < chemistryTolerance) {
      // lots of rules
      if (checkFertile(_flirter1, _flirter2)) {
        if ((person[_flirter1].gender == "male" && person[_flirter2].gender == "female") || (person[_flirter2].gender == "male" && person[_flirter1].gender == "female")) {
          if (abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= chemistryAppearanceTolerance) {
            if (abs((int)Float.parseFloat(person[_flirter1].trait.get("money").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("money").toString())) <= chemistryMoneyTolerance) {
              if (abs((int)Float.parseFloat(person[_flirter1].trait.get("religiosity").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("religiosity").toString())) <= chemistryReligiosityTolerance) {
                procreate = true;
              }
            }
          }
        }
      }
    }
    return procreate;
  }

  // takes two parents and creates an offspring person object
  public void sex(Person comp1, Person comp2) {
    Person newPerson;
    String _gender = "";
    int _pickGender = (int)random(0, 1.99f);
    int _parent1 = comp1.uniqueID;
    int _parent2 = comp2.uniqueID;
    String _nationality;
    switch (_pickGender) {
    case 0:
      _gender = "male";
      break;
    case 1:
      _gender = "female";
      break;
    } 

    int _getNationality = (int)random(0, 4.99f);
    int[] percentage = {
      0, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15
    }; 
    int _getMbti = (int)random(0, 99.99f);

    // #PeopleChange
    newPerson = new Person(numPeople, percentage[_getMbti], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (int)random(90, 400), (int)random(48, 96), 
    0, 1, 0, 0, 0, 0, 0, 0, 0, 0, "person"+Integer.toString(numPeople+1), _gender, _parent1, 
    _parent2, _getNationality, currentYear, (int)random(0, 6.99f), 0, (int)random(0, 1.99f), randomX(_getNationality), randomY(_getNationality));

    Object characteristic, characteristic2;
    for (Enumeration e = comp1.trait.keys() ; e.hasMoreElements() ;) {
      characteristic = e.nextElement();
      int compy1 = (int)(Float.parseFloat(comp1.trait.get(characteristic).toString()));
      int compy2 = (int)(Float.parseFloat(comp2.trait.get(characteristic).toString()));
      newPerson.trait.put(characteristic, punnettSquare(compy1, compy2));
    }

    person[numPeople] = newPerson;

    // takes parents' traits and combines them to see what child will get
    if (person[numPeople].parent1 != -1 && person[numPeople].parent2 != -1) {
      person[numPeople].strength = punnettSquare(person[_parent1].strength, person[_parent2].strength);
      person[numPeople].intelligence = punnettSquare(person[_parent1].intelligence, person[_parent2].intelligence);
      person[numPeople].wisdom = punnettSquare(person[_parent1].wisdom, person[_parent2].wisdom);
      person[numPeople].charisma = punnettSquare(person[_parent1].charisma, person[_parent2].charisma);
      person[numPeople].stamina = punnettSquare(person[_parent1].stamina, person[_parent2].stamina);
      person[numPeople].wit = punnettSquare(person[_parent1].wit, person[_parent2].wit);
      person[numPeople].humor = punnettSquare(person[_parent1].humor, person[_parent2].humor);
      person[numPeople].education = punnettSquare(person[_parent1].education, person[_parent2].education);
      person[numPeople].creativity = punnettSquare(person[_parent1].creativity, person[_parent2].creativity);
      person[numPeople].responsibility = punnettSquare(person[_parent1].responsibility, person[_parent2].responsibility);
      person[numPeople].discipline = punnettSquare(person[_parent1].discipline, person[_parent2].discipline);
      person[numPeople].honesty = punnettSquare(person[_parent1].honesty, person[_parent2].honesty);
      person[numPeople].religiosity = punnettSquare(person[_parent1].religiosity, person[_parent2].religiosity);
      person[numPeople].entrepreneurialism = punnettSquare(person[_parent1].entrepreneurialism, person[_parent2].entrepreneurialism);
      person[numPeople].appearance = punnettSquare(person[_parent1].appearance, person[_parent2].appearance);
      person[numPeople].money = punnettSquare(person[_parent1].money, person[_parent2].money);
      person[numPeople].gracefulness = punnettSquare(person[_parent1].gracefulness, person[_parent2].gracefulness);
      person[numPeople].stress = punnettSquare(person[_parent1].stress, person[_parent2].stress);
      person[numPeople].health = punnettSquare(person[_parent1].health, person[_parent2].health);
      person[numPeople].luck = punnettSquare(person[_parent1].luck, person[_parent2].luck);
      person[numPeople].talentMath = punnettSquare(person[_parent1].talentMath, person[_parent2].talentMath);
      person[numPeople].talentArt = punnettSquare(person[_parent1].talentArt, person[_parent2].talentArt);
      person[numPeople].talentSports = punnettSquare(person[_parent1].talentSports, person[_parent2].talentSports);
    }

    // add new person to physics world with appropriate springs
    physics.addParticle(person[numPeople]);
    godSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], god, godRL, godGravity));

    // creates random rest length, saves to Person object and makes new spring between person and nation
    int tempNationRL = round(random(nationRL1, nationRL2));
    nationSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], nation[person[numPeople].nationality], tempNationRL, random(nationGravity1, nationGravity2)));
    physics.addSpring(nationSpringArray.get(nationSpringArray.size()-1));
    person[numPeople].nationRL = tempNationRL;
    if (person[numPeople].parent1 != -1 && person[numPeople].parent2 != -1) {
      int tempParent1RL = round(random(parentRL1, parentRL2));
      int tempParent2RL = round(random(parentRL1, parentRL2));
      parentSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], person[person[numPeople].parent1], tempParent1RL, random(parentGravity1, parentGravity2)));
      physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
      person[numPeople].parent1Spring = parentSpringArray.size()-1;
      parentSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], person[person[numPeople].parent2], tempParent1RL, random(parentGravity1, parentGravity2)));
      physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
      person[numPeople].parent2Spring = parentSpringArray.size()-1;
      person[numPeople].parent1RL = tempParent1RL;
      person[numPeople].parent2RL = tempParent2RL;
      parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[numPeople], person[person[numPeople].parent1], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
      person[numPeople].parent1MinDistanceSpring = parentMinDistanceSpringArray.size()-1;
      physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
      parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[numPeople], person[person[numPeople].parent2], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
      person[numPeople].parent2MinDistanceSpring = parentMinDistanceSpringArray.size()-1;
      physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
    }
    int tempReligionRL = round(random(religionRL1, religionRL2));
    religionSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], religion[person[numPeople].religion], tempReligionRL, random(religionGravity1, religionGravity2)));
    physics.addSpring(religionSpringArray.get(religionSpringArray.size()-1));
    person[numPeople].religionRL = tempReligionRL;
    if (person[numPeople].parent1 != -1 && person[numPeople].parent2 != -1) {
      person[_parent1].children++;
      person[_parent2].children++;
    }
    numPeople++;
    populationSize++;
  }

  public boolean beer(int _flirter1, int _flirter2) {
    if ((abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString()) - (int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= 2) && checkFertile(_flirter1, _flirter2)) {
      return true;
    }
    else { 
      return false;
    }
  }

  // can the person have a baby now?
  public boolean checkFertile(int _flirter1, int _flirter2) {
    int female = 0;
    boolean fertile = false;
    if (person[_flirter1].gender == "female" && person[_flirter1].alive == true) {
      female = 1;
      if ((currentYear - person[_flirter1].lastBaby > gestationRate) && (person[_flirter1].age >= 18) && (person[_flirter1].age <= maxReproductiveAge) && (person[_flirter2].age >= 18)) {
        fertile = true;
      }
    }
    else if (person[_flirter2].gender == "female" && person[_flirter1].alive == true) {
      female = 2;
      if ((currentYear - person[_flirter2].lastBaby > gestationRate) && (person[_flirter2].age >= 18) && (person[_flirter2].age <= maxReproductiveAge) && (person[_flirter1].age >= 18)) {
        fertile = true;
      }
    }
    return fertile;
  }

//  int punnettSquare(int comp1, int comp2) {
//    int mutation = (int)random(0, 9.99);
//    int offspringTraitVal = ceil((comp1 + comp2) / 2);
//    if (mutation < 3) {
//      offspringTraitVal += (int)random(-2, 2);
//    } 
//    if (offspringTraitVal < 1) { 
//      offspringTraitVal = 1;
//    }
//    if (offspringTraitVal > 10) { 
//      offspringTraitVal = 10;
//    }
//    return offspringTraitVal;
//  }

  // modified this from the above, which just averages trait values
  // this function will decide whether to take the either parent's full value and then see if it mutates an offset of, say, 4 points
  public int punnettSquare(int comp1, int comp2) {
    int mutation = (int)random(0, 50);
    int dominance = floor(random(0, 1.99f));
    int crossover = 0;
    if (dominance == 0) {
      crossover = comp1;
    } 
    else {
      crossover = comp2;
    }
    if (mutation < 2) {
      crossover += (int)random(-4, 4);
    }
    // don't want it to be out of bounds
    // TODO: fix so it can mutate only one way if parent is 1 or 10
    if (crossover < 1) { crossover = 1; } else if (crossover > 10) { crossover = 10; }
    return crossover;
  }
}

public class Nation extends VerletParticle2D {
  Hashtable trait = new Hashtable();
  int uniqueID, security, innovation, jobOpportunity, immigrationPolicy,lifeExpectancy,
  education, sanitation, standardOfLiving,pollution,biodiversity,crime,politicalFreedom,nutrition;
  String nameNation;
  int[] colorBaseArray = new int[3]; // need base colors for each nation
  int[][] colorBaseArraySquares = new int[numNationCharacteristics][3];
  float[][] traitPosArray = new float[numNationCharacteristics][2]; // save x,y for all characteristics
  
  Nation(int _uniqueID, String _nameNation, float x, float y, int _security, int _innovation, int _jobOpportunity, int _immigrationPolicy,
  int _lifeExpectancy, int _education, int _sanitation, int _standardOfLiving, int _pollution, int _biodiversity, int _crime, int _politicalFreedom, int _nutrition) {
    super(x, y);
    trait.put("security", _security);
    trait.put("innovation", _innovation);
    trait.put("jobOpportunity", _jobOpportunity);
    trait.put("immigrationPolicy", _immigrationPolicy);
    trait.put("lifeExpectancy", _lifeExpectancy);
    trait.put("education", _education);
    trait.put("sanitation", _sanitation);
    trait.put("standardOfLiving", _standardOfLiving);
    trait.put("pollution", _pollution);
    trait.put("biodiversity", _biodiversity);
    trait.put("crime", _crime);
    trait.put("politicalFreedom", _politicalFreedom);
    trait.put("nutrition", _nutrition);
    uniqueID = _uniqueID;
    nameNation = _nameNation;
    security = _security;
    innovation = _innovation;
    jobOpportunity = _jobOpportunity;
    immigrationPolicy = _immigrationPolicy;
    lifeExpectancy = _lifeExpectancy;
    education = _education;
    sanitation = _sanitation;
    standardOfLiving = _standardOfLiving;
    pollution = _pollution;
    biodiversity = _biodiversity;
    crime = _crime;
    politicalFreedom = _politicalFreedom;
    nutrition = _nutrition;
    
    // each Nation object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = PApplet.parseInt(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numNationCharacteristics;j++) {
      colorBaseArraySquares[j][0] = 50; //abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = 50; //abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = 50; //abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numNationCharacteristics;i++) {
      traitPosArray[i][0] = x+random(-150, 150);
      traitPosArray[i][1] = y+random(-150, 150);
    }
  }
  
  public void display(int setOpacity) {
    stroke(0);
    int i = 0;

    // loads rect diameters from hashtable, iterates thru all characteristics
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorBaseArray[0]-i*30, colorBaseArray[1]-i*20, colorBaseArray[2], setOpacity);
      Object characteristic = e.nextElement();
      Object diameterRect = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterRect.toString());
      int diam = ceil(_diameter)*4;
      noStroke();
      rect(traitPosArray[i][0], traitPosArray[i][1], diam, diam);
      i++;
    }

    // displays main rect for each Nation
    fill(colorBaseArray[0],colorBaseArray[1],colorBaseArray[2]);
    rectMode(CENTER);
    rect(x,y,nationBoxSize,nationBoxSize);
    rectMode(CORNER);
    fill(255,255,0,200);
    textSize(globalLabelS);
    text(nameNation, x+15, y+15);
  } // display for Nation
  
  public float getXPos() {
    return x;
  }
  
  public float getYPos() {
    return y;
  }
  
}
class Particle extends VerletParticle2D {

  Particle(float x, float y) {
    super(x,y);
  }

  // All we're doing really is adding a display() function to a VerletParticle
  public void display() {
    fill(175);
    stroke(0);
    ellipse(x,y,16,16);
  }
}
class Person extends VerletParticle2D {
  Hashtable trait = new Hashtable();
  String namePerson, gender;
  int parent1, parent2, uniqueID, pWeight, pHeight, age, mbti, lastBaby,
  nationality, religion, children,strength,intelligence,wisdom,charisma,stamina,wit,
  humor,education,creativity,responsibility,discipline,honesty,religiosity,entrepreneurialism,
  appearance,money,gracefulness,stress,health,luck,talentMath,talentArt,talentSports, happiness,
  employed, parent1RL, parent2RL, religionRL, nationRL, parent1Spring, parent2Spring, parent1MinDistanceSpring, parent2MinDistanceSpring;
  int[] colorBaseArray = new int[3]; // need base colors for each person
  float[][] traitPosArray = new float[numCharacteristics][2]; // save x,y for all characteristics
  float[][] traitPosArray_orig = new float[numCharacteristics][2]; // original copy of coords
  boolean alive;

  // constructor
  Person(int _uniqueID, int _mbti, int _stress, int _health, int _gracefulness, int _luck, int _talentMath, int _talentArt, int _talentSports, int _strength,
  int _intelligence, int _wisdom, int _charisma, int _stamina, int _wit, int _humor, int _pWeight, int _pHeight,
  int _education, int _age, int _creativity, int _responsibility, int _discipline, int _honesty, int _religiosity,
  int _entrepreneurialism, int _appearance, int _money, String _namePerson, String _gender, int _parent1,
  int _parent2, int _nationality, int lastBaby, int _religion, int _children, int _employed, float x, float y) {
    super(x, y);
    trait.put("strength", _strength);
    trait.put("intelligence", _intelligence);
    trait.put("wisdom", _wisdom);
    trait.put("charisma", _charisma);
    trait.put("stamina", _stamina);
    trait.put("wit", _wit);
    trait.put("humor", _humor);
    trait.put("education", _education);
    trait.put("creativity", _creativity);
    trait.put("responsibility", _responsibility);
    trait.put("discipline", _discipline);
    trait.put("honesty", _honesty);
    trait.put("religiosity", _religiosity);
    trait.put("entrepreneurialism", _entrepreneurialism);
    trait.put("appearance", _appearance);
    trait.put("money", _money);
    trait.put("gracefulness", _gracefulness);
    trait.put("stress", _stress);
    trait.put("health", _health);
    trait.put("luck", _luck);
    trait.put("math talent", _talentMath);
    trait.put("art talent", _talentArt);
    trait.put("sports talent", _talentSports);
    
    strength = _strength;
    intelligence = _intelligence;
    wisdom = _wisdom;
    charisma = _charisma;
    stamina = _stamina;
    wit = _wit;
    humor = _humor;
    education = _education;
    creativity = _creativity;
    responsibility = _responsibility;
    discipline = _discipline;
    honesty = _honesty;
    religiosity = _religiosity;
    entrepreneurialism = _entrepreneurialism;
    appearance = _appearance;
    money = _money;
    gracefulness = _gracefulness;
    stress = _stress;
    health = _health;
    luck = _luck;
    talentMath = _talentMath;
    talentArt = _talentArt;
    talentSports = _talentSports;
    employed = _employed;
    
    happiness = 0;
    
    lastBaby = 0;
    uniqueID = _uniqueID;
    namePerson = _namePerson;
    parent1 = _parent1;
    parent2 = _parent2;
    gender = _gender;
    children = _children;
    nationality = _nationality;
    religion = _religion;
    alive = true;
    mbti = _mbti;
    //xPos = int(random(50, width-100));
    //yPos = int(random(50, height-50));
    pWeight = _pWeight;
    pHeight = _pHeight;
    age = _age;
    
    parent1RL = 0;
    parent2RL = 0;
    religionRL = 0;
    nationRL = 0;

    // each Person object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = PApplet.parseInt(random(255));
    }

    // generates random position for all the characteristics
    for (int i=0;i<numCharacteristics;i++) {
      traitPosArray_orig[i][0] = PApplet.parseInt(random(-30, 30));
      traitPosArray_orig[i][1] = PApplet.parseInt(random(-30, 30));
    }
    
  } // end Person method

  public void display(int setOpacity) {
    
    // draws container circle
    noStroke();
    if (alive == true) {
      fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], PApplet.parseInt(setOpacity/2));
    }
    else {
      fill(0xffBCBCBC, setOpacity);
    }
    ellipse(x, y, 70, 70);

    // loads circle diameters from hashtable, iterates thru all characteristics
    int i = 0;
    stroke(0);
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      if (alive == true) {
        fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], setOpacity);
      }
      else {
        fill(0xffBCBCBC, setOpacity);
      }
      Object characteristic = e.nextElement();
      Object diameterCirc = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterCirc.toString());
      int diam = ceil(_diameter)*2;
      noStroke();
      
      // generates random position for all the characteristics
    for (int j=0;j<numCharacteristics;j++) {
      traitPosArray[j][0] = traitPosArray_orig[j][0] + x;
      traitPosArray[j][1] = traitPosArray_orig[j][1] + y;
    }

      ellipse(traitPosArray[i][0], traitPosArray[i][1], diam, diam);
      i++;
    }

    // displays main circle for each Person
    stroke(255);
    if (alive == true) {
      fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], 255);
    }
    else { // dead!
      fill(0xffBCBCBC, 10);
    }
    ellipse(x, y, personNodeSize, personNodeSize); // overall rating, in middle
    textSize(globalLabelS-1);
    int _pHeight = round(pHeight * 5 * age / 100);
    int _pWeight = round(pWeight * 2 * age / 100);
    if (_pHeight > pHeight) { _pHeight = pHeight; }
    if (_pWeight > pWeight) { _pWeight = pWeight; }
    int pFeet = round(_pHeight / 12);
    int pInches = _pHeight % 12;
    fill(255,200);
    if (namePerson == "God") {
      text(namePerson, x+15, y+15);
    }
    else {
      text(namePerson + " (" + gender + ", " + age + "yo, " + getFuncs.getNationality(nationality) + ")", x+15, y+15);
      text(_pWeight + "lbs, " + pFeet + "'" + pInches + "\"", x+15, y+25);
    }

  } // display for Person
  
} // end Person class
public class Religion extends VerletParticle2D {
  Hashtable trait = new Hashtable();
  int uniqueID, commercial, morality, hierarchy,portability;
  String nameReligion;
  int[] colorBaseArray = new int[3]; // need base colors for each religion
  int[][] colorBaseArraySquares = new int[numReligionCharacteristics][3];
  int[][] traitPosArray = new int[numReligionCharacteristics][2]; // save x,y for all characteristics
  
  Religion(int _uniqueID, String _nameReligion, int x, int y, int _commercial, int _morality, int _hierarchy, int _portability) {
    super(x, y);
    trait.put("commercial", _commercial);
    trait.put("morality", _morality);
    trait.put("hierarchy", _hierarchy);
    trait.put("portability", _portability);
    uniqueID = _uniqueID;
    nameReligion = _nameReligion;
    commercial = _commercial;
    morality = _morality;
    hierarchy = _hierarchy;
    portability = _portability;
    
    // each religion object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = PApplet.parseInt(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numReligionCharacteristics;j++) {
      colorBaseArraySquares[j][0] = 50; //abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = 50; //abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = 50; //abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numReligionCharacteristics;i++) {
      traitPosArray[i][0] = x+PApplet.parseInt(random(-150, 150));
      traitPosArray[i][1] = y+PApplet.parseInt(random(-150, 150));
    }
  }
  
  public void display(int setOpacity) {
    stroke(0);
    int i = 0;

    // loads ellipse diameters from hashtable, iterates thru all characteristics
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorBaseArray[0]-i*30, colorBaseArray[1]-i*20, colorBaseArray[2], setOpacity);
      Object characteristic = e.nextElement();
      Object diameterEllipse = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterEllipse.toString());
      int diam = ceil(_diameter)*4;
      noStroke();
      ellipse(traitPosArray[i][0], traitPosArray[i][1], diam, PApplet.parseInt(diam*0.66f));
      i++;
    }

    // displays main ellipse for each religion
    fill(colorBaseArray[0],colorBaseArray[1],colorBaseArray[2]);
    ellipse(x,y,religionBoxSize,PApplet.parseInt(religionBoxSize*0.66f));
    fill(3, 255, 181,200);
    textSize(globalLabelS);
    text(nameReligion, x+15, y+15);
  } // display for religion
  
  public float getXPos() {
    return x;
  }
  
  public float getYPos() {
    return y;
  }
  
}
public class Rituals {

  Rituals() {
  }

  public void funeral(boolean funeralsRecognized) {
    println(funeralsRecognized);
    for (int i=0; i<numPeople; i++) {
      if (person[i].parent1 != -1 && person[i].parent2 != -1 && person[i].alive == false) {
        if (funeralsRecognized == true) {
          physics.removeSpring(parentSpringArray.get(person[i].parent1Spring));
          physics.removeSpring(parentSpringArray.get(person[i].parent2Spring));
          physics.removeSpring(parentMinDistanceSpringArray.get(person[i].parent1MinDistanceSpring));
          physics.removeSpring(parentMinDistanceSpringArray.get(person[i].parent2MinDistanceSpring));
          person[i].lock();
          person[i].display(0);
        }
        else { // TODO: re-reference spring after it's recreated?
          parentSpringArray.add(new VerletConstrainedSpring2D(person[i], person[person[i].parent1], person[i].parent1RL, random(parentGravity1, parentGravity2)));
          person[i].parent1Spring = parentSpringArray.size()-1;
          physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
          parentSpringArray.add(new VerletConstrainedSpring2D(person[i], person[person[i].parent2], person[i].parent2RL, random(parentGravity1, parentGravity2)));
          person[i].parent2Spring = parentSpringArray.size()-1;
          physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
          parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[i], person[person[i].parent1], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
          physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
          parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[i], person[person[i].parent2], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
          physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
          person[i].unlock();
          person[i].display(1);
        }
      }
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
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "genetic_crossing_noc_final" });
  }
}
