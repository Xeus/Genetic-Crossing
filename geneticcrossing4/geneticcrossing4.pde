// Ben Turner
// NYU-ITP, Intro to Computational Media

// Exercise with objects, classes, constructors

// v.0.3: offspring inherits avg of parents' traits

// need to add: aging, death, randomization, distancing  

// need these functions for all the vars and walking thru them
import java.util.Enumeration;
import java.util.Hashtable;

long iterationCount = 0;
int currentYear = 0;

Person person[]; // init Person object array
Person god;
Nation nation[]; // init Nation object array
Religion religion[];

boolean toggleNoLoop = false; // toggles space bar for stopping/starting loop

int numCharacteristics = 21; // total characteristics per person (see Person class)
int numPeople = 4; // initialize starting # of ppl
int maxPeople = 30; // so it won't go nuts; don't forget to limit and/or raise this when needed
int flirter1, flirter2; // picks random flirters
int globalLabelS = 9; // text size of labels

int personNodeSize = 10; // size of black circle representing each person

int numNationCharacteristics = 4;
int numNations = 5; // starting # of nations
int maxNations = 10; // so won't go out of bounds
int nationNodeSize=200; // size of nation blocs
int nationBoxSize = 10;
int nationSpacing = 200;

int numReligionCharacteristics = 4;
int numReligions = 7; // starting # of nations
int maxReligions = 10; // so won't go out of bounds
int religionNodeSize=200; // size of nation blocs
int religionBoxSize = 10;
int religionSpacing = 200;

int[][] colorArray = new int[numCharacteristics][3]; // need colors for all characteristics
int[] characteristicsArray = new int[numCharacteristics];

int buttonX = 15;
int buttonY = 30;
int buttonSizeX = 150;
int buttonSizeY = 25;

boolean geneticMap = true;

void setup() {
  size(1200, 700);
  smooth();

  person = new Person[maxPeople];
  nation = new Nation[maxNations];
  religion = new Religion[maxReligions];

  // init all person objects & nations
  for (int i=0;i<maxPeople;i++) {
    person[i] = new Person(i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "",
    -1, -1, -1, 0, 0, 0, 0);
  }
  for (int i=0;i<maxNations;i++) {
    nation[i] = new Nation(i, "", 0, 0, 0, 0, 0, 0);
  }
  for (int i=0;i<maxReligions;i++) {
    religion[i] = new Religion(i, "", 0, 0, 0, 0, 0, 0);
  }

  nation[0] = new Nation(0, "USA", 200, 200, (int)random(70, 80), (int)random(80, 100), (int)random(60, 80), (int)random(40, 60));
  nation[1] = new Nation(1, "EU", 400, 300, (int)random(40, 50), (int)random(60, 70), (int)random(50, 60), (int)random(50, 60));
  nation[2] = new Nation(2, "China", 200, 500, (int)random(70, 80), (int)random(40, 50), (int)random(40, 50), (int)random(30, 40));
  nation[3] = new Nation(3, "South America", 700, 200, (int)random(20, 40), (int)random(30, 40), (int)random(30, 40), (int)random(40, 50));
  nation[4] = new Nation(4, "Africa", 800, 400, (int)random(10, 30), (int)random(20, 30), (int)random(20, 30), (int)random(20, 30));

  religion[0] = new Religion(0, "Buddhism", 100, 100, (int)random(20, 40), (int)random(20, 30), (int)random(0, 10), (int)random(20, 30));
  religion[1] = new Religion(1, "Christianity", 400, 200, (int)random(50, 60), (int)random(80, 100), (int)random(60, 80), (int)random(60, 70));
  religion[2] = new Religion(2, "Confucianism", 100, 600, (int)random(30, 40), (int)random(80, 100), (int)random(0, 20), (int)random(20, 40));
  religion[3] = new Religion(3, "Hinduism", 300, 400, (int)random(70, 80), (int)random(80, 100), (int)random(90, 100), (int)random(60, 70));
  religion[4] = new Religion(4, "Islam", 300, 300, (int)random(70, 80), (int)random(80, 100), (int)random(60, 80), (int)random(60, 70));
  religion[5] = new Religion(5, "Judaism", 800, 600, (int)random(90, 100), (int)random(50, 70), (int)random(60, 80), (int)random(20, 40));
  religion[6] = new Religion(6, "Taoism", 400, 600, (int)random(20, 40), (int)random(20, 40), (int)random(10, 30), (int)random(10, 30));

  god = new Person(-1, 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 0, 10, 0, 10, 10, 10, 10, 10, 10, 10, 10, "God", "null", -2, -2, -1, 0, -1, 1100, 600);

  // create some Adams and Eves and Sumerians
  person[0] = new Person(0, 0, 2, 6, 10, 8, 1, 6, 10, 10, 7, 6, 8, 6, 180, 67, 10, 80, 10, 10, 9, 8, 5, 2, 1, 7, "Albert Einstein", "male", -1, -1, 1, 0, 5, nation[1].getXPos() + int(random(-nationSpacing, nationSpacing)), nation[1].getXPos() + int(random(-nationSpacing, nationSpacing)));
  person[1] = new Person(1, 8, 9, 9, 2, 5, 3, 2, 2, 1, 8, 5, 2, 2, 130, 70, 4, 30, 2, 6, 7, 5, 2, 2, 10, 9, "Gisele Bundchen", "female", -1, -1, 3, 0, 1, nation[3].getXPos() + int(random(-nationSpacing, nationSpacing)), nation[3].getXPos() + int(random(-nationSpacing, nationSpacing)));
  person[2] = new Person(2, 15, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 300), (int)random(1, 100), (int)random(1, 10), 
  (int)random(1, 120), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), "person3", "male", -1, -1, 4, 0, 6, nation[4].getXPos() + int(random(-nationSpacing, nationSpacing)), nation[4].getXPos() + int(random(-nationSpacing, nationSpacing)));
  person[3] = new Person(3, 11, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 300), (int)random(1, 100), (int)random(1, 10), 
  (int)random(1, 120), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), "person4", "female", -1, -1, 0, 0, 4, nation[0].getXPos() + int(random(-nationSpacing, nationSpacing)), nation[0].getXPos() + int(random(-nationSpacing, nationSpacing)));

  // generates random color standard for all the person characteristics
  for (int i=0;i<numCharacteristics;i++) {
    for (int j=0;j<3;j++) {
      colorArray[i][j] = int(random(255));
    }
  }
  
}

void draw() {

  if (iterationCount % 20 == 0) {
    currentYear++;
  }
  //System.out.println(currentYear + " " + iterationCount);
  if (geneticMap == true) {
    background(0);
    fill(255);
    textSize(globalLabelS);

    drawNations();
    drawReligions();
    drawLegend();
    drawPeople();
    drawPopUpBoxPeople();
    drawPopUpBoxNations();
    drawPopUpBoxReligions();
    
    drawInterface();
  }
  else {
    background(0);
    stroke(0);

    drawChromosomalStainings();
  }

  flirt(); // as people do

  // is there an attraction that may create offspring?
  if (numPeople <= maxPeople-1) { // or else person[] will go out of bounds when creating new person
    if (chemistry(flirter1, flirter2)) {
      sex(person[flirter1], person[flirter2]);
      if (person[flirter1].gender == "female") {
        person[flirter1].lastBaby = currentYear;
      }
      else if (person[flirter2].gender == "female") {
        person[flirter2].lastBaby = currentYear;
      }
    }
    else if (beer(flirter1, flirter2)) { // beer is social lubrication
      sex(person[flirter1], person[flirter2]);
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

// gets two unique male-female pairs to test compatibility
void flirt() {
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
boolean chemistry(int _flirter1, int _flirter2) {
  boolean procreate = false;
  int randomTest = (int)random(10);
  if (randomTest < 4) {
    // lots of rules
    if (checkFertile(_flirter1, _flirter2)) {
      if ((person[_flirter1].gender == "male" && person[_flirter2].gender == "female") || (person[_flirter2].gender == "male" && person[_flirter1].gender == "female")) {
      if (abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= 1) {
        if (abs((int)Float.parseFloat(person[_flirter1].trait.get("money").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("money").toString())) <= 2) {
          if (abs((int)Float.parseFloat(person[_flirter1].trait.get("religiosity").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("religiosity").toString())) <= 1) {
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
void sex(Person comp1, Person comp2) {
  Person newPerson;
  String _gender = "";
  int _pickGender = (int)random(0, 1.99);
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

  int _getNationality = (int)random(0, 4.99);
  int[] percentage = {0,1,2,2,2,2,2,3,3,3,3,3,4,5,6,6,6,6,6,7,7,7,7,7,8,8,8,8,8,8,8,8,8,8,8,8,8,9,9,9,9,9,9,9,9,9,9,9,9,9,10,10,10,10,10,10,11,11,11,11,11,11,12,12,12,12,12,12,12,12,12,12,12,12,12,13,13,13,13,13,13,13,13,13,13,13,13,13,14,14,14,14,14,14,15,15,15,15,15,15}; 
  int _getMbti = (int)random(0, 99.99);

  newPerson = new Person(numPeople, percentage[_getMbti], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (int)random(1, 400), (int)random(1, 90), 
  0, (int)random(1, 110), 0, 0, 0, 0, 0, 0, 0, 0, "person"+Integer.toString(numPeople+1), _gender, _parent1, 
  _parent2, _getNationality, currentYear, (int)random(0,6.99), nation[_getNationality].getXPos() + int(random(-nationSpacing, nationSpacing)), 
  nation[_getNationality].getYPos() + int(random(-nationSpacing, nationSpacing)));

  Object characteristic, characteristic2;
  for (Enumeration e = comp1.trait.keys() ; e.hasMoreElements() ;) {
    characteristic = e.nextElement();
    int compy1 = (int)(Float.parseFloat(comp1.trait.get(characteristic).toString()));
    int compy2 = (int)(Float.parseFloat(comp2.trait.get(characteristic).toString()));
    newPerson.trait.put(characteristic, punnettSquare(compy1, compy2));
  }

  person[numPeople] = newPerson;
  numPeople++;
}

boolean beer(int _flirter1, int _flirter2) {
  if (abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString()) - (int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= 1) {
    return true;
  }
  else { 
    return false;
  }
}

boolean checkFertile(int _flirter1, int _flirter2) {
  int female = 0;
  boolean fertile = false;
  if (person[_flirter1].gender == "female") {
    female = 1;
    if (currentYear - person[_flirter1].lastBaby > 10) {
      fertile = true;
    }
  }
  else if (person[_flirter2].gender == "female") {
    female = 2;
    if (currentYear - person[_flirter2].lastBaby > 10) {
      fertile = true;
    }
  }
  return fertile;
}

int punnettSquare(int comp1, int comp2) {
  int mutation = (int)random(0,9.99);
  int offspringTraitVal = ceil((comp1 + comp2) / 2);
  if (mutation < 1) {
    offspringTraitVal += (int)random(-2,2);
  } 
  if (offspringTraitVal < 1) { offspringTraitVal = 1; }
  if (offspringTraitVal > 10) { offspringTraitVal = 10; }
  //System.out.println(comp1 + " " + comp2 + " " + offspringTraitVal);
  return offspringTraitVal;
}

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

void drawPeople() {
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
    }
  }
  god.display(50);
}

void drawFamilyLines(int i, int lineOpacity) {
  // draw familial connection lines
  
  if (person[i].parent1 != -1) {
    stroke(0, 0, 255, lineOpacity);
    line(person[person[i].parent1].xPos, person[person[i].parent1].yPos, person[i].xPos, person[i].yPos);
    line(person[person[i].parent2].xPos, person[person[i].parent2].yPos, person[i].xPos, person[i].yPos);
    stroke(0, 255, 0, lineOpacity);
    line(person[person[i].parent1].xPos, person[person[i].parent1].yPos, person[person[i].parent2].xPos, person[person[i].parent2].yPos);
  }
  else {
    stroke(255, 255, 255, lineOpacity);
    line(god.xPos, god.yPos, person[i].xPos, person[i].yPos);
  }
  
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

void drawNations() {
  for (int i=0;i<numNations;i++) {
    int security = (int)Float.parseFloat(nation[i].trait.get("security").toString());
    if (security != 0) {
      nation[i].display(10);
    }
  }
}

void drawReligions() {
  for (int i=0;i<numReligions;i++) {
    int commercial = (int)Float.parseFloat(religion[i].trait.get("commercial").toString());
    if (commercial != 0) {
      religion[i].display(10);
    }
  }
}

void drawLegend() {
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
void drawPopUpBoxPeople() {
  String[] mbti = new String[2];
  
  for (int i=0;i<numPeople;i++) {
    int strength = (int)Float.parseFloat(person[i].trait.get("strength").toString());
    if (strength != 0) {
      if (mouseX >= person[i].xPos-(int(personNodeSize/2)) && mouseX <= person[i].xPos+(int(personNodeSize/2)) && mouseY >= person[i].yPos-(int(personNodeSize/2)) && mouseY <= person[i].yPos+(int(personNodeSize/2))) {
        drawFamilyLines(i, 255);
        person[i].display(255);
        if (mousePressed) {
        textSize(10);
        int drawBoxX = mouseX;
        int drawBoxY = mouseY;
        int drawBoxWidth = 150;
        int drawBoxHeight = 400;
        if ((drawBoxY + drawBoxHeight) > height) {
          drawBoxY = drawBoxY - 15 - abs(height-(drawBoxY + drawBoxHeight));
        }
        fill(255);
        stroke(0);
        
        rect(drawBoxX+35, drawBoxY+15, drawBoxWidth, drawBoxHeight);
        fill(0);
        int pFeet = round(person[i].pHeight / 12);
        int pInches = person[i].pHeight % 12;
        mbti = getMBTI(person[i].mbti);
        text(person[i].namePerson + " (" + person[i].gender + ", " + person[i].age + "yo)\n" +
        getNationality(person[i].nationality) + ", " + getReligion(person[i].religion) + "\n" + mbti[0] + ", " + mbti[1] + "\n" + person[i].pWeight +
        "lbs, " + pFeet + "'" + pInches + "\"", drawBoxX+45, drawBoxY+30);
        int j = 0;
        int k = 0; // distance between each item in the legend

        // draws popup window for values from each person node
        Object characteristic;
        for (Enumeration e = person[1].trait.keys() ; e.hasMoreElements() ;) {
          characteristic = e.nextElement();
          fill(colorArray[j][0], colorArray[j][1], colorArray[j][2]);
          text(characteristic.toString(), drawBoxX+75, drawBoxY+95+k);
          text(person[i].trait.get(characteristic).toString(), drawBoxX+45, drawBoxY+95+k);
          j++;
          k += 15;
        }
      }
      }
    }
  }
}

// when you mouseover a node, some basic data is displayed
void drawPopUpBoxNations() {
  for (int i=0;i<numNations;i++) {
    int strength = (int)Float.parseFloat(nation[i].trait.get("security").toString());
    if (strength != 0) {
      if ((mouseX >= nation[i].xPos-int(nationBoxSize/2)) && (mouseX <= nation[i].xPos+int(nationBoxSize/2)) && (mouseY >= nation[i].yPos-int(nationBoxSize/2)) && (mouseY <= nation[i].yPos+int(nationBoxSize/2))) {
        nation[i].display(150);
        textSize(14);
        int drawBoxX = mouseX;
        int drawBoxY = mouseY;
        int drawBoxWidth = 180;
        int drawBoxHeight = 150;
        if ((drawBoxY + drawBoxHeight) > height) {
          drawBoxY = drawBoxY + 15 - abs(height-(drawBoxY + drawBoxHeight));
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
void drawPopUpBoxReligions() {
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

void drawInterface() {
  fill(255);
  textSize(globalLabelS);
  text("blue = parent -> child, green = 2 parents, yellow = nationality, turquoise = religion", 15, 15);
  noStroke();
  if ((mouseX >= buttonX) && (mouseX <= buttonX+buttonSizeX) && (mouseY >= buttonY) && (mouseY <= buttonY+buttonSizeY)) {
    fill(#FFD95D);
  }
  else {
    fill(#FFC400);
  }
  rect(buttonX, buttonY, buttonSizeX, buttonSizeY);

  textSize(10);
  fill(0);
  text("view chromosomal stainings", buttonX+8, buttonY+15);
}

// loads chromosomal stainings view
void drawChromosomalStainings() {
  noStroke();
  if ((mouseX >= buttonX) && (mouseX <= buttonX+buttonSizeX) && (mouseY >= buttonY) && (mouseY <= buttonY+buttonSizeY)) {
    fill(#FFD95D);
  }
  else {
    fill(#FFC400);
  }
  rect(buttonX, buttonY, buttonSizeX, buttonSizeY);

  textSize(10);
  fill(0);
  text("view time universe", buttonX+8, buttonY+15);
  int i = 0; // iterator for which characteristic to print for the legend
  int j = 20; // distance between each item in the legend
  for (int h=0;h<numPeople;h++) {
    for (Enumeration e = person[h].trait.keys() ; e.hasMoreElements() ;) {
      Object characteristic = e.nextElement();
      Object diameterCirc = person[h].trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterCirc.toString());
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
      ellipse(width/2 + i * 20, 80 + j * h, _diameter * 3, _diameter * 3);
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
      i++;
    }
    textSize(14);
    fill(255);
    textAlign(RIGHT);
    text(person[h].namePerson, width/2-40, 85 + j * h);
    textAlign(BASELINE);
    i = 0;
  }
  int k = 15;
  int l = 0;
  pushMatrix();
  translate(width/2, height/2);
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
}

String getNationality(int _getNationality) {
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

String getReligion(int _getReligion) {
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

String[] getMBTI(int _getMBTI) {
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
}

