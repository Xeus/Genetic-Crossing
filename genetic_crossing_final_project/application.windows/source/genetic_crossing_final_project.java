import processing.core.*; 

import java.util.Enumeration; 
import java.util.Hashtable; 

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

public class genetic_crossing_final_project extends PApplet {

// Ben Turner
// NYU-ITP, Intro to Computational Media
//
// documentation:
// 
// http://blog.benturner.com/2011/11/10/icm-final-project-genetic-crossing/
// http://blog.benturner.com/2011/10/16/icm-genetic-crossing-part-2/
// http://blog.benturner.com/2011/10/13/icm-genetic-crossings/


// If you add traits or variables to Person.person, you must add variables to 4 places, found by searching
// #PeopleChange (3 in main, 1 in MatingDance).

// need these functions for all the vars and walking thru them
// (used these to play with, ended up wishing I just used integer variables & a switch{} function to define them)



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

public void setup() {
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
      colorArray[i][j] = PApplet.parseInt(random(255));
    }
  }
}

// calc random x position relative to person's nationality
public int randomX(int _nation) {
  int randomX = nation[_nation].getXPos() + PApplet.parseInt(random(-nationSpacing, nationSpacing));
  if (randomX > width) { 
    randomX = width + (width + randomX - 30);
  }
  return randomX;
}

// calc random y position relative to person's nationality
public int randomY(int _nation) {
  int randomY = nation[_nation].getYPos() + PApplet.parseInt(random(-nationSpacing, nationSpacing));
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
        person[i].display(50);
        /* person[i].xPos = person[i].xPos + calcCultureMoveX(i, person[i].xPos);
         int finalY = calcCultureMoveY(i, person[i].yPos);
         int speedY = ceil(abs(person[i].yPos - finalY) / 20);
         if (person[i].yPos < finalY) person[i].yPos = person[i].yPos + speedY;
         */
        drawFamilyLines(i, 25);
        drawFuncs.drawHappiness(i);
      }
    }
    // show God
    god.display(50);
  }

  public void drawFamilyLines(int i, int lineOpacity) { // draw familial connection lines

    stroke(255, 255, 255, lineOpacity);
    line(god.xPos, god.yPos, person[i].xPos, person[i].yPos);

    if (person[i].parent1 != -1) {
      stroke(0, 0, 255, lineOpacity);
      line(person[person[i].parent1].xPos, person[person[i].parent1].yPos, person[i].xPos, person[i].yPos);
      line(person[person[i].parent2].xPos, person[person[i].parent2].yPos, person[i].xPos, person[i].yPos);
      stroke(0, 255, 0, lineOpacity);
      line(person[person[i].parent1].xPos, person[person[i].parent1].yPos, person[person[i].parent2].xPos, person[person[i].parent2].yPos);
    }

    // nation connection lines
    stroke(255, 255, 0, lineOpacity);
    switch (person[i].nationality) {
    case 0:
      line(person[i].xPos, person[i].yPos, nation[0].xPos, nation[0].yPos);
      break;
    case 1:
      line(person[i].xPos, person[i].yPos, nation[1].xPos, nation[1].yPos);
      break;
    case 2:
      line(person[i].xPos, person[i].yPos, nation[2].xPos, nation[2].yPos);
      break;
    case 3:
      line(person[i].xPos, person[i].yPos, nation[3].xPos, nation[3].yPos);
      break;
    case 4:
      line(person[i].xPos, person[i].yPos, nation[4].xPos, nation[4].yPos);
      break;
    }

    // religion connection lines
    stroke(3, 255, 181, lineOpacity);
    switch (person[i].religion) {
    case 0:
      line(person[i].xPos, person[i].yPos, religion[0].xPos, religion[0].yPos);
      break;
    case 1:
      line(person[i].xPos, person[i].yPos, religion[1].xPos, religion[1].yPos);
      break;
    case 2:
      line(person[i].xPos, person[i].yPos, religion[2].xPos, religion[2].yPos);
      break;
    case 3:
      line(person[i].xPos, person[i].yPos, religion[3].xPos, religion[3].yPos);
      break;
    case 4:
      line(person[i].xPos, person[i].yPos, religion[4].xPos, religion[4].yPos);
      break;
    case 5:
      line(person[i].xPos, person[i].yPos, religion[5].xPos, religion[5].yPos);
      break;
    case 6:
      line(person[i].xPos, person[i].yPos, religion[6].xPos, religion[6].yPos);
      break;
    }
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
        if (mouseX >= person[i].xPos-(PApplet.parseInt(personNodeSize/2)) && mouseX <= person[i].xPos+(PApplet.parseInt(personNodeSize/2)) && mouseY >= person[i].yPos-(PApplet.parseInt(personNodeSize/2)) && mouseY <= person[i].yPos+(PApplet.parseInt(personNodeSize/2))) {
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
        if ((mouseX >= nation[i].xPos-PApplet.parseInt(nationBoxSize/2)) && (mouseX <= nation[i].xPos+PApplet.parseInt(nationBoxSize/2)) && (mouseY >= nation[i].yPos-PApplet.parseInt(nationBoxSize/2)) && (mouseY <= nation[i].yPos+PApplet.parseInt(nationBoxSize/2))) {
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
        if ((mouseX >= religion[i].xPos) && (mouseX <= religion[i].xPos+religionBoxSize) && (mouseY >= religion[i].yPos) && (mouseY <= religion[i].yPos+religionBoxSize)) {
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
    text("blue = parent -> child, green = 2 parents, yellow = nationality, turquoise = religion", 15, 15);

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
    text("stats & chromosome stainings", buttonX+4, buttonY+15);

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
        fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
        ellipse(width/2 + 120 + i * 20, karyogramPush + 80 + j * h, _diameter * 3, _diameter * 3);
        fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
        i++;
      }
      textSize(14);
      fill(255);
      textAlign(RIGHT);
      text(person[h].namePerson, width/2+100, karyogramPush + 85 + j * h);
      textAlign(BASELINE);
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
      text(characteristic.toString(), 280, 4+l * j);
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
      if (personCharsRealTotal == 0) { personCharsRealTotal = 1; }
      if (personCharsIdealTotal == 0) { personCharsIdealTotal = 1; }
      ratioPotential = PApplet.parseFloat(personCharsRealTotal) / PApplet.parseFloat(personCharsIdealTotal);

      text(nation[i].nameNation, 200, yLine);
      yLine += 15;
      text(" population: " + populationNation[i], 200, yLine);
      yLine += 15;
      text(" raw well-being: " + personCharsNationRaw, 200, yLine);
      yLine += 15;
      text(" actual/potential well-being ratio: " + nf(ratioPotential,0,2), 200, yLine);
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
    fill(happinessColor,200);
    text(happinessText, person[i].xPos+15, person[i].yPos+34);
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
   totalMoveX = nation[i].xPos - personX;
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
   totalMoveY = (nation[i].yPos + personY) / 2;
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

    // takes parents' traits and combines them to see what child will get
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

    person[numPeople] = newPerson;
    person[_parent1].children++;
    person[_parent2].children++;
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

  public int punnettSquare(int comp1, int comp2) {
    int mutation = (int)random(0, 9.99f);
    int offspringTraitVal = ceil((comp1 + comp2) / 2);
    if (mutation < 3) {
      offspringTraitVal += (int)random(-2, 2);
    } 
    if (offspringTraitVal < 1) { 
      offspringTraitVal = 1;
    }
    if (offspringTraitVal > 10) { 
      offspringTraitVal = 10;
    }
    //System.out.println(comp1 + " " + comp2 + " " + offspringTraitVal);
    return offspringTraitVal;
  }
}

public class Nation {
  Hashtable trait = new Hashtable();
  int uniqueID, security, innovation, jobOpportunity, immigrationPolicy,lifeExpectancy,
  education, sanitation, standardOfLiving,pollution,biodiversity,crime,politicalFreedom,nutrition;
  private int xPos;
  private int yPos;
  String nameNation;
  int[] colorBaseArray = new int[3]; // need base colors for each nation
  int[][] colorBaseArraySquares = new int[numNationCharacteristics][3];
  int[][] traitPosArray = new int[numNationCharacteristics][2]; // save x,y for all characteristics
  
  Nation(int _uniqueID, String _nameNation, int _xPos, int _yPos, int _security, int _innovation, int _jobOpportunity, int _immigrationPolicy,
  int _lifeExpectancy, int _education, int _sanitation, int _standardOfLiving, int _pollution, int _biodiversity, int _crime, int _politicalFreedom, int _nutrition) {
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
    xPos = _xPos;
    yPos = _yPos;
    
    // each Nation object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = PApplet.parseInt(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numNationCharacteristics;j++) {
      colorBaseArraySquares[j][0] = abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numNationCharacteristics;i++) {
      traitPosArray[i][0] = xPos+PApplet.parseInt(random(-150, 150));
      traitPosArray[i][1] = yPos+PApplet.parseInt(random(-150, 150));
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
    rect(xPos,yPos,nationBoxSize,nationBoxSize);
    rectMode(CORNER);
    fill(255,255,0,200);
    textSize(globalLabelS);
    text(nameNation, xPos+15, yPos+15);
  } // display for Nation
  
  public int getXPos() {
    return xPos;
  }
  
  public int getYPos() {
    return yPos;
  }
  
}
class Person {
  Hashtable trait = new Hashtable();
  String namePerson, gender;
  int parent1, parent2, uniqueID, xPos, yPos, pWeight, pHeight, age, mbti, lastBaby,
  nationality, religion, children,strength,intelligence,wisdom,charisma,stamina,wit,
  humor,education,creativity,responsibility,discipline,honesty,religiosity,entrepreneurialism,
  appearance,money,gracefulness,stress,health,luck,talentMath,talentArt,talentSports, happiness, employed;
  int[] colorBaseArray = new int[3]; // need base colors for each person
  int[][] traitPosArray = new int[numCharacteristics][2]; // save x,y for all characteristics
  boolean alive;

  // constructor
  Person(int _uniqueID, int _mbti, int _stress, int _health, int _gracefulness, int _luck, int _talentMath, int _talentArt, int _talentSports, int _strength,
  int _intelligence, int _wisdom, int _charisma, int _stamina, int _wit, int _humor, int _pWeight, int _pHeight,
  int _education, int _age, int _creativity, int _responsibility, int _discipline, int _honesty, int _religiosity,
  int _entrepreneurialism, int _appearance, int _money, String _namePerson, String _gender, int _parent1,
  int _parent2, int _nationality, int lastBaby, int _religion, int _children, int _employed, int _xPos, int _yPos) {
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
    xPos = _xPos;
    yPos = _yPos;
    //xPos = int(random(50, width-100));
    //yPos = int(random(50, height-50));
    pWeight = _pWeight;
    pHeight = _pHeight;
    age = _age;

    // each Person object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = PApplet.parseInt(random(255));
    }

    // generates random position for all the characteristics
    for (int i=0;i<numCharacteristics;i++) {
      traitPosArray[i][0] = xPos+PApplet.parseInt(random(-30, 30));
      traitPosArray[i][1] = yPos+PApplet.parseInt(random(-30, 30));
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
    ellipse(xPos, yPos, 70, 70);

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

      ellipse(traitPosArray[i][0], traitPosArray[i][1], diam, diam);
      i++;
    }

    // displays main circle for each Person
    stroke(255);
    if (alive == true) {
      fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], 255);
    }
    else {
      fill(0xffBCBCBC, setOpacity);
    }
    ellipse(xPos, yPos, personNodeSize, personNodeSize); // overall rating, in middle
    textSize(globalLabelS-1);
    int _pHeight = round(pHeight * 5 * age / 100);
    int _pWeight = round(pWeight * 2 * age / 100);
    if (_pHeight > pHeight) { _pHeight = pHeight; }
    if (_pWeight > pWeight) { _pWeight = pWeight; }
    int pFeet = round(_pHeight / 12);
    int pInches = _pHeight % 12;
    fill(255,200);
    if (namePerson == "God") {
      text(namePerson, xPos+15, yPos+15);
    }
    else {
      text(namePerson + " (" + gender + ", " + age + "yo, " + getFuncs.getNationality(nationality) + ")", xPos+15, yPos+15);
      text(_pWeight + "lbs, " + pFeet + "'" + pInches + "\"", xPos+15, yPos+25);
    }

  } // display for Person
  
} // end Person class

public class Religion {
  Hashtable trait = new Hashtable();
  int uniqueID, commercial, morality, hierarchy,portability;
  private int xPos;
  private int yPos;
  String nameReligion;
  int[] colorBaseArray = new int[3]; // need base colors for each religion
  int[][] colorBaseArraySquares = new int[numReligionCharacteristics][3];
  int[][] traitPosArray = new int[numReligionCharacteristics][2]; // save x,y for all characteristics
  
  Religion(int _uniqueID, String _nameReligion, int _xPos, int _yPos, int _commercial, int _morality, int _hierarchy, int _portability) {
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
    xPos = _xPos;
    yPos = _yPos;
    
    // each religion object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = PApplet.parseInt(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numReligionCharacteristics;j++) {
      colorBaseArraySquares[j][0] = abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numReligionCharacteristics;i++) {
      traitPosArray[i][0] = xPos+PApplet.parseInt(random(-150, 150));
      traitPosArray[i][1] = yPos+PApplet.parseInt(random(-150, 150));
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
    ellipse(xPos,yPos,religionBoxSize,PApplet.parseInt(religionBoxSize*0.66f));
    fill(3, 255, 181,200);
    textSize(globalLabelS);
    text(nameReligion, xPos+15, yPos+15);
  } // display for religion
  
  public int getXPos() {
    return xPos;
  }
  
  public int getYPos() {
    return yPos;
  }
  
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "genetic_crossing_final_project" });
  }
}
