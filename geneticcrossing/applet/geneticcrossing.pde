// Ben Turner
// NYU-ITP, Intro to Computational Media

// Exercise with objects, classes, constructors

// need these functions for all the vars and walking thru them
import java.util.Enumeration;
import java.util.Hashtable;

// init the Person objects
Person person1;
Person person2;
Person albertEinstein;
Person giseleBundchen;
int numPeople = 4;

// total of 18 characteristics per person right now
int numCharacteristics = 18;
int[][] colorArray = new int[numCharacteristics][3];
int[] characteristicsArray = new int[numCharacteristics];

void setup() {
  size(600, 400);
  smooth();
  frameRate(1);

  /* reference:
   * float _strength, float _intelligence, float _wisdom, 
   * float _charisma, float _stamina, float _wit, float _humor, 
   * float _pWeight, float _pHeight, float _education, float _age, 
   * float _creativity, float _responsibility, float _discipline, 
   * float _honesty, float _religiosity, float _entrepreneurialism, 
   * float _appearance, String _namePerson
   */

  person1 = new Person(random(0, 10), random(0, 10), random(0, 10), random(0, 10), 
  random(0, 10), random(0, 10), random(0, 10), random(0, 300), random(0, 100), random(0, 10), 
  random(0, 120), random(0, 10), random(0, 10), random(0, 10), random(0, 10), random(0, 10), 
  random(0, 10), random(0, 10), "person1");
  person2 = new Person(random(0, 10), random(0, 10), random(0, 10), random(0, 10), 
  random(0, 10), random(0, 10), random(0, 10), random(0, 300), random(0, 100), random(0, 10), 
  random(0, 120), random(0, 10), random(0, 10), random(0, 10), random(0, 10), random(0, 10), 
  random(0, 10), random(0, 10), "person2");
  albertEinstein = new Person(6, 10, 10, 7, 6, 8, 6, 180, 67, 10, 80, 10, 10, 9, 8, 5, 2, 1, "Albert Einstein");
  giseleBundchen = new Person(2, 2, 1, 8, 5, 2, 2, 130, 70, 4, 30, 2, 6, 7, 5, 2, 2, 10, "Gisele Bundchen");
  
  // generates random color standard for all the characteristics
  for (int i=0;i<numCharacteristics;i++) {
    for (int j=0;j<3;j++) {
      colorArray[i][j] = int(random(255));
    }
  }
}

void draw() {
  background(255);
  person1.display();
  person2.display();
  albertEinstein.display();
  giseleBundchen.display();
  Person person3 = sex(giseleBundchen, albertEinstein);
  person3.display();
  int i = 0;
  int j = 30;
  
  // loads circle diameters from hashtable, iterates thru all characteristics
  for (Enumeration e = person1.trait.keys() ; e.hasMoreElements() ;) {
    fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
    rect(405, j, 5, 5);
    fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
    text(e.nextElement().toString(), 415, j+7);
    i++;
    j = j + 15;
  }

  noLoop();
}

Person sex(Person comp1, Person comp2) {
  Person newPerson;
  newPerson = new Person(random(0, 10), random(0, 10), random(0, 10), random(0, 10), 
  random(0, 10), random(0, 10), random(0, 10), random(0, 300), random(0, 100), random(0, 10), 
  random(0, 120), random(0, 10), random(0, 10), random(0, 10), random(0, 10), random(0, 10), 
  random(0, 10), random(0, 10), "person3");
  int _appearance;
  Object diameterCirc1 = comp1.trait.get("appearance");
  Object diameterCirc2 = comp2.trait.get("appearance");
  float _diameter1 = Float.parseFloat(diameterCirc1.toString());
  float _diameter2 = Float.parseFloat(diameterCirc2.toString());
  _appearance = round(_diameter1 + _diameter2)*2;
  newPerson.trait.put("appearance", _appearance);
  numPeople++;
  return newPerson;
}


