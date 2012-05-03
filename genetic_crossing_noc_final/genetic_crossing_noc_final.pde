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

import java.util.Enumeration;
import java.util.Hashtable;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.geom.*;
import org.json.*;

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
float godGravity = 0.05;
int godRL = 400;
int parentRL1 = 80;
int parentRL2 = 150;
int parentMinDistanceRL1 = 50;
int parentMinDistanceRL2 = 80;
float parentGravity1 = 0.1;
float parentGravity2 = 0.3;
int nationRL1 = 200;
int nationRL2 = 400;
float nationGravity1 = 0.005;
float nationGravity2 = 0.01;
int religionRL1 = 450;
int religionRL2 = 600;
float religionGravity1 = 0.001;
float religionGravity2 = 0.008;

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

void setup() {
  size(1400, 800);
  smooth();

  // size of CA buttons on stainings screen
  caButtonX = width/2+75;
  for (int h=0;h<maxPeople;h++) {
    caButtonY[h] = 110 + 20 * h;
  }

  // Initialize the physics
  physics=new VerletPhysics2D();
  physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.0)));

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
      colorArray[i][j] = int(random(255));
    }
  }

  // Accessing Galapag.us characteristics
  //String resetURL = "/reset";
  //JSONObject resetResult = fetchJSON(resetURL);

  //timer = new Timer(5000);
  //timer.start();
}

// calc random x position relative to person's nationality
float randomX(int _nation) {
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
float randomY(int _nation) {
  float randomY = nation[_nation].getYPos() + random(-nationSpacing, nationSpacing);
  if (randomY > height) { 
    randomY = height + (height + randomY - 30);
  }
  else if (randomY < 0) {
    randomY = 0 - randomY + 30;
  }
  return randomY;
}

void draw() {

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

void keyPressed() {
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

void mouseReleased() {

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

JSONObject fetchJSON(String URL_PATH) {

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

void getMainData() {

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

