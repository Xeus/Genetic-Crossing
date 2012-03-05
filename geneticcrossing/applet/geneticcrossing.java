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

public class geneticcrossing extends PApplet {

// Ben Turner
// NYU-ITP, Intro to Computational Media

// Exercise with objects, classes, constructors

// need these functions for all the vars and walking thru them



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

public void setup() {
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
      colorArray[i][j] = PApplet.parseInt(random(255));
    }
  }
}

public void draw() {
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

public Person sex(Person comp1, Person comp2) {
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


class Person {
  Hashtable trait = new Hashtable();
  int xPos = PApplet.parseInt(random(50, 350));
  int yPos = PApplet.parseInt(random(50, 350));
  String namePerson;

  // constructor
  Person(float _strength, float _intelligence, float _wisdom, 
  float _charisma, float _stamina, float _wit, float _humor, 
  float _pWeight, float _pHeight, float _education, float _age, 
  float _creativity, float _responsibility, float _discipline, 
  float _honesty, float _religiosity, float _entrepreneurialism, 
  float _appearance, String _namePerson) {
    trait.put("strength", _strength);
    trait.put("intelligence", _intelligence);
    trait.put("wisdom", _wisdom);
    trait.put("charisma", _charisma);
    trait.put("stamina", _stamina);
    trait.put("wit", _wit);
    trait.put("humor", _humor);
    _pWeight = round(_pWeight / 20);
    trait.put("pWeight", _pWeight);
    _pHeight = round(_pHeight / 10);
    trait.put("pHeight", _pHeight);
    trait.put("education", _education);
    trait.put("age", _age);
    trait.put("creativity", _creativity);
    trait.put("responsibility", _responsibility);
    trait.put("discipline", _discipline);
    trait.put("honesty", _honesty);
    trait.put("religiosity", _religiosity);
    trait.put("entrepreneurialism", _entrepreneurialism);
    trait.put("appearance", _appearance);
    namePerson = _namePerson;
  } // end Person method

  public void display() {
    int i = 0;

    // loads circle diameters from hashtable, iterates thru all characteristics
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
      Object diameterCirc = trait.get(e.nextElement());
      float _diameter = Float.parseFloat(diameterCirc.toString());
      int diam = round(_diameter)*2;
      ellipse(xPos+PApplet.parseInt(random(-40, 40)), yPos+PApplet.parseInt(random(-40, 40)), diam, diam);
      i++;
    }

    // displays main circle for each Person
    fill(0);
    ellipse(xPos, yPos, 20, 20); // overall rating, in middle
    text(this.namePerson, xPos+15, yPos+15);
  } // display for Person
} // end Person class

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "geneticcrossing" });
  }
}
