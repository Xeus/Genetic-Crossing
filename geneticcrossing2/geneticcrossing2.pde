// Ben Turner
// NYU-ITP, Intro to Computational Media

// Exercise with objects, classes, constructors

// need these functions for all the vars and walking thru them
import java.util.Enumeration;
import java.util.Hashtable;

Person person[]; // init Person object array
Nation nation[]; // init Nation object array

boolean toggleNoLoop = false; // toggles space bar for stopping/starting loop

int numCharacteristics = 19; // total characteristics per person (see Person class)
int numPeople = 4; // initialize starting # of ppl
int maxPeople = 30; // so it won't go nuts; don't forget to limit and/or raise this when needed
int flirter1, flirter2; // picks random flirters

int personNodeSize = 20; // size of black circle representing each person
int ageDivider = 5; // divides age for drawing circle
int weightDivider = 40; // divides weight for drawing circle
int heightDivider = 10; // divides height for drawing circle

int numNations = 4; // starting # of nations
int maxNations = 10; // so won't go out of bounds
int nationNodeSize=200; // size of nation blocs

int[][] colorArray = new int[numCharacteristics][3]; // need colors for all characteristics
int[] characteristicsArray = new int[numCharacteristics];

void setup() {
  size(600, 700);
  smooth();
  frameRate(5);

  person = new Person[maxPeople];
  nation = new Nation[maxNations];

  // init all person objects & nations
  for (int i=0;i<maxPeople;i++) {
    person[i] = new Person(i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "", -1, -1);
  }
  for (int i=0;i<maxNations;i++) {
    nation[i] = new Nation(i, "", 0, 0, 0, 0, 0, 0);
  }

  // create some Adams and Eves and Sumerians
  person[0] = new Person(0, 6, 10, 10, 7, 6, 8, 6, 180, 67, 10, 80, 10, 10, 9, 8, 5, 2, 1, 7, "Albert Einstein", "male", -1, -1);
  person[1] = new Person(1, 2, 2, 1, 8, 5, 2, 2, 130, 70, 4, 30, 2, 6, 7, 5, 2, 2, 10, 9, "Gisele Bundchen", "female", -1, -1);
  person[2] = new Person(2, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 300), (int)random(1, 100), (int)random(1, 10), 
  (int)random(1, 120), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1,10), "person3", "male", -1, -1);
  person[3] = new Person(2, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 300), (int)random(1, 100), (int)random(1, 10), 
  (int)random(1, 120), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1,10), "person4", "female", -1, -1);
  
  nation[0] = new Nation(0, "USA", 200, 100, (int)random(1, 100), (int)random(1, 100), (int)random(1, 100), (int)random(1, 100));
  nation[1] = new Nation(0, "European Union", 200, 400, (int)random(1, 100), (int)random(1, 100), (int)random(1, 100), (int)random(1, 100));
  nation[2] = new Nation(0, "China", 200, 600, (int)random(1, 100), (int)random(1, 100), (int)random(1, 100), (int)random(1, 100));

  // generates random color standard for all the characteristics
  stroke(0);
  for (int i=0;i<numCharacteristics;i++) {
    for (int j=0;j<3;j++) {
      colorArray[i][j] = int(random(255));
    }
  }
}

void draw() {
  background(255);
  fill(0);
  text("blue = parent -> child, green = 2 parents", 15, 15);
  
  drawNations();
  drawLegend();
  drawPeople();
  drawPopUpBox();

  flirt(); // as people do
  
  // is there an attraction that may create offspring?
  if (numPeople < maxPeople-1) { // or else person[] will go out of bounds when creating new person
    if (chemistry(flirter1, flirter2)) {
      numPeople++;
      person[numPeople] = sex(person[flirter1], person[flirter2]);
    }
  }

  // unluckyNightOut();
  
}

/* void unluckyNightOut() {
  int[] xPos = new int[numPeople];
  int[] yPos = new int[numPeople];
  for (int i=0;i<numPeople;i++) {
    xPos[i] = person[i].xPos;
    yPos[i] = person[i].yPos;
  }
} */


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
    if ((person[_flirter1].gender == "male" && person[_flirter2].gender == "female") || (person[_flirter2].gender == "male" && person[_flirter1].gender == "female")) {
      if (abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= 2) {
        if (abs((int)Float.parseFloat(person[_flirter1].trait.get("money").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("money").toString())) <= 3) {
          if (abs((int)Float.parseFloat(person[_flirter1].trait.get("religiosity").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("religiosity").toString())) <= 1) {
            procreate = true;
          }
        }
      }
    }
  }
  return procreate;
}

// takes two parents and creates an offspring person object
Person sex(Person comp1, Person comp2) {
  Person newPerson;
  String _gender = "";
  int _pickGender = (int)random(0,1.99);
  int _parent1 = comp1.uniqueID;
  int _parent2 = comp2.uniqueID;
  switch (_pickGender) {
  case 0:
    _gender = "male";
    break;
  case 1:
    _gender = "female";
    break;
  } 
  newPerson = new Person(numPeople, (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 300), (int)random(1, 100), (int)random(1, 10), 
  (int)random(1, 120), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), (int)random(1, 10), 
  (int)random(1, 10), (int)random(1, 10), (int)random(1,10), "person"+Integer.toString(numPeople), _gender, _parent1, _parent2);
  
  int _appearance;
  int _diameter1 = (int)Float.parseFloat(comp1.trait.get("appearance").toString());
  int _diameter2 = (int)Float.parseFloat(comp2.trait.get("appearance").toString());
  _appearance = ceil((_diameter1 + _diameter2)/2);
  newPerson.trait.put("appearance", _appearance);
  
  return newPerson;
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
      person[i].display();
      /* person[i].xPos = person[i].xPos + calcCultureMoveX(i, person[i].xPos);
      int finalY = calcCultureMoveY(i, person[i].yPos);
      int speedY = ceil(abs(person[i].yPos - finalY) / 20);
      if (person[i].yPos < finalY) person[i].yPos = person[i].yPos + speedY;
      */
      // draw familial connection lines
      if (person[i].parent1 != -1) {
        stroke(0,0,255,100);
        line(person[person[i].parent1].xPos, person[person[i].parent1].yPos, person[i].xPos, person[i].yPos);
        line(person[person[i].parent2].xPos, person[person[i].parent2].yPos, person[i].xPos, person[i].yPos);
        stroke(0,255,0,100);
        line(person[person[i].parent1].xPos, person[person[i].parent1].yPos, person[person[i].parent2].xPos, person[person[i].parent2].yPos);
        stroke(0);
      }
    }
  }
}

void drawNations() {
  for (int i=0;i<numNations;i++) {
    int security = (int)Float.parseFloat(nation[i].trait.get("security").toString());
    if (security != 0) {
      nation[i].display();
    }
  }
}

void drawLegend() {
  int i = 0; // iterator for which characteristic to print for the legend
  int j = 30; // distance between each item in the legend
  stroke(0);
  
  // loads circle diameters from hashtable, iterates thru all characteristics
  // person[0] arbitrary usage for pulling out characteristics
  for (Enumeration e = person[1].trait.keys() ; e.hasMoreElements() ;) {
    fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
    rect(445, j, 5, 5);
    fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
    text(e.nextElement().toString(), 455, j+7);
    i++;
    j = j + 15;
  }  
  
}

// when you mouseover a node, some basic data is displayed
void drawPopUpBox() {
  for (int i=0;i<numPeople;i++) {
    int strength = (int)Float.parseFloat(person[i].trait.get("strength").toString());
    if (strength != 0) {
      if (mouseX >= person[i].xPos-(int(personNodeSize/2)) && mouseX <= person[i].xPos+(int(personNodeSize/2)) && mouseY >= person[i].yPos-(int(personNodeSize/2)) && mouseY <= person[i].yPos+(int(personNodeSize/2))) {
        int drawBoxX = mouseX;
        int drawBoxY = mouseY;
        int drawBoxWidth = 200;
        int drawBoxHeight = 350;
        if ((drawBoxY + drawBoxHeight) > height) {
          drawBoxY = drawBoxY - 30 - abs(height-(drawBoxY + drawBoxHeight));
        }
        fill(255);
        stroke(0);
        rect(drawBoxX+15,drawBoxY+15,drawBoxWidth,drawBoxHeight);
        fill(0);
        text(person[i].gender + ", " + (int)Float.parseFloat(person[i].trait.get("age").toString()) * ageDivider + "yo, " + (int)Float.parseFloat(person[i].trait.get("pWeight").toString()) * weightDivider + "lbs, " + (int)Float.parseFloat(person[i].trait.get("pHeight").toString()) * heightDivider + "\"" , drawBoxX+30, drawBoxY+30);
        int j = 0;
        int k = 0; // distance between each item in the legend

        // draws popup window for values from each person node
        Object characteristic;
        for (Enumeration e = person[1].trait.keys() ; e.hasMoreElements() ;) {
          characteristic = e.nextElement();
          fill(colorArray[j][0], colorArray[j][1], colorArray[j][2]);
          text(characteristic.toString(), drawBoxX+45, drawBoxY+55+k);
          text(person[i].trait.get(characteristic).toString(), drawBoxX+20, drawBoxY+55+k);
          j++;
          k += 15;
        }
      }
    }
  }  
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
