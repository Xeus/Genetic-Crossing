// Ben Turner
// NYU-ITP, Intro to Computational Media
//
// documentation:
// 
// http://blog.benturner.com/2011/12/08/genetic-crossings-icm-final-project-presentation/
// http://blog.benturner.com/2011/11/10/icm-final-project-genetic-crossing/
// http://blog.benturner.com/2011/10/16/icm-genetic-crossing-part-2/
// http://blog.benturner.com/2011/10/13/icm-genetic-crossings/


// If you add traits or variables to Person.person, you must add variables to 4 places, found by searching
// #PeopleChange (3 in main, 1 in MatingDance).

// need these functions for all the vars and walking thru them
// (used these to play with, ended up wishing I just used integer variables & a switch{} function to define them)
import java.util.Enumeration;
import java.util.Hashtable;

long iterationCount = 0; // # of draw()s
int currentYear = 0; // keeps track of years since sketch start

Person person[]; // init Person object array
Person god; // init God
Nation nation[];
Religion religion[];
DrawFuncs drawFuncs = new DrawFuncs();
GetFuncs getFuncs = new GetFuncs();
MatingDance matingDance = new MatingDance();
Evolutions evolutions = new Evolutions();
Death death = new Death();

boolean toggleNoLoop = false; // toggles space bar for stopping/starting loop

int numCharacteristics = 31; // total characteristics per person (see Person class)
int numPeople = 5; // initialize starting # of ppl & counter for names of new people
int populationSize = 5; // size of current population
int equilibriumPopulation = 40; // so babies will be born after people die, keeps population fresh up to maxPeople
int maxPeople = 90; // so it won't go nuts; don't forget to limit and/or raise this when needed
int maxReproductiveAge = 50; // how old a woman can be and still have children
int deathAgeBegin = 60; // when death functions kick in to see if people will die
int flirter1, flirter2; // picks random flirters
int globalLabelS = 9; // text size of labels

int chemistryTolerance = 4; // max limit of attraction during flirting/chemistry
int chemistryAppearanceTolerance = 1; // max difference in peoples' appearance
int chemistryMoneyTolerance = 2; // max diff in peoples' money
int chemistryReligiosityTolerance = 1; // max diff in peoples' religiosity
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
int karyogramButtonUpX = 520;
int karyogramButtonUpY = 600;
int karyogramButtonDownX = 520;
int karyogramButtonDownY = 650;
int karyogramButtonSize = 30;
int scrollingAmount = 15;

// controls whether on the genetic map screen or the stats screen
boolean geneticMap = true;

void setup() {
  size(1200, 700);
  smooth();
  
  person = new Person[maxPeople];
  nation = new Nation[maxNations];
  religion = new Religion[maxReligions];

  // #PeopleChange init all person objects & nations
  for (int i=0;i<maxPeople;i++) {
    person[i] = new Person(i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "", 
    -1, -1, -1, 0, 0, 0, 0, 0, 0);
  }
  for (int i=0;i<maxNations;i++) {
    nation[i] = new Nation(i, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }
  for (int i=0;i<maxReligions;i++) {
    religion[i] = new Religion(i, "", 0, 0, 0, 0, 0, 0);
  }

  // #PeopleChange
  nation[0] = new Nation(0, "USA", 200, 200, (int)random(70, 80), (int)random(80, 100), (int)random(60, 80), (int)random(40, 60), (int)random(70, 80), 
  (int)random(70, 80), (int)random(80, 90), (int)random(70, 80), (int)random(40, 50), (int)random(70, 90), (int)random(30, 40), (int)random(70, 80), (int)random(70, 80));
  nation[1] = new Nation(1, "EU", 500, 500, (int)random(40, 50), (int)random(60, 70), (int)random(50, 60), (int)random(50, 60), (int)random(90, 100), 
  (int)random(90, 100), (int)random(80, 100), (int)random(80, 90), (int)random(80, 100), (int)random(50, 60), (int)random(70, 90), (int)random(90, 100), (int)random(90, 100));
  nation[2] = new Nation(2, "China", 200, 500, (int)random(70, 80), (int)random(40, 50), (int)random(40, 50), (int)random(30, 40), (int)random(60, 70), 
  (int)random(60, 70), (int)random(60, 70), (int)random(40, 60), (int)random(10, 20), (int)random(30, 40), (int)random(50, 60), (int)random(20, 30), (int)random(40, 60));
  nation[3] = new Nation(3, "South America", 800, 200, (int)random(20, 40), (int)random(30, 40), (int)random(30, 40), (int)random(40, 50), (int)random(60, 70), 
  (int)random(50, 60), (int)random(30, 40), (int)random(50, 60), (int)random(20, 30), (int)random(70, 80), (int)random(60, 70), (int)random(50, 60), (int)random(50, 60));
  nation[4] = new Nation(4, "Africa", 900, 600, (int)random(10, 30), (int)random(20, 30), (int)random(20, 30), (int)random(20, 30), (int)random(20, 30), 
  (int)random(20, 30), (int)random(10, 30), (int)random(20, 30), (int)random(20, 30), (int)random(70, 80), (int)random(30, 50), (int)random(30, 50), (int)random(20, 40));

  religion[0] = new Religion(0, "Buddhism", 100, 100, (int)random(20, 40), (int)random(20, 30), (int)random(0, 10), (int)random(20, 30));
  religion[1] = new Religion(1, "Christianity", 400, 200, (int)random(50, 60), (int)random(80, 100), (int)random(60, 80), (int)random(60, 70));
  religion[2] = new Religion(2, "Confucianism", 100, 600, (int)random(30, 40), (int)random(80, 100), (int)random(0, 20), (int)random(20, 40));
  religion[3] = new Religion(3, "Hinduism", 300, 400, (int)random(70, 80), (int)random(80, 100), (int)random(90, 100), (int)random(60, 70));
  religion[4] = new Religion(4, "Islam", 650, 500, (int)random(70, 80), (int)random(80, 100), (int)random(60, 80), (int)random(60, 70));
  religion[5] = new Religion(5, "Judaism", 900, 400, (int)random(90, 100), (int)random(50, 70), (int)random(60, 80), (int)random(20, 40));
  religion[6] = new Religion(6, "Taoism", 1000, 300, (int)random(20, 40), (int)random(20, 40), (int)random(10, 30), (int)random(10, 30));

  // who art in Heaven
  god = new Person(-1, 0, 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 0, 10, 0, 10, 10, 10, 10, 10, 10, 10, 10, "God", "null", -2, -2, -1, 0, -1, 0, 1, 1100, 600);

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

  // generates random color standard for all the person characteristics
  for (int i=0;i<numCharacteristics;i++) {
    for (int j=0;j<3;j++) {
      colorArray[i][j] = int(random(255));
    }
  }
}

// calc random x position relative to person's nationality
int randomX(int _nation) {
  int randomX = nation[_nation].getXPos() + int(random(-nationSpacing, nationSpacing));
  if (randomX > width) { 
    randomX = width + (width + randomX - 30);
  }
  return randomX;
}

// calc random y position relative to person's nationality
int randomY(int _nation) {
  int randomY = nation[_nation].getYPos() + int(random(-nationSpacing, nationSpacing));
  if (randomY > height) { 
    randomY = height + (height + randomY - 30);
  }
  return randomY;
}

public void draw() {
  
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
}

