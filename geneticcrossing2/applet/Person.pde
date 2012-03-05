class Person {
  Hashtable trait = new Hashtable();
  String namePerson, gender;
  int parent1, parent2, uniqueID, xPos, yPos;

  // constructor
  Person(int _uniqueID, int _strength, int _intelligence, int _wisdom, 
  int _charisma, int _stamina, int _wit, int _humor, 
  int _pWeight, int _pHeight, int _education, int _age, 
  int _creativity, int _responsibility, int _discipline, 
  int _honesty, int _religiosity, int _entrepreneurialism, 
  int _appearance, int _money, String _namePerson, String _gender, int _parent1, int _parent2) {
    trait.put("strength", _strength);
    trait.put("intelligence", _intelligence);
    trait.put("wisdom", _wisdom);
    trait.put("charisma", _charisma);
    trait.put("stamina", _stamina);
    trait.put("wit", _wit);
    trait.put("humor", _humor);
    trait.put("pWeight", ceil(_pWeight / 40));
    trait.put("pHeight", ceil(_pHeight / 10));
    trait.put("education", _education);
    trait.put("age", ceil(_age / 5));
    trait.put("creativity", _creativity);
    trait.put("responsibility", _responsibility);
    trait.put("discipline", _discipline);
    trait.put("honesty", _honesty);
    trait.put("religiosity", _religiosity);
    trait.put("entrepreneurialism", _entrepreneurialism);
    trait.put("appearance", _appearance);
    trait.put("money", _money);
    uniqueID = _uniqueID;
    namePerson = _namePerson;
    xPos = int(random(50, 350));
    yPos = int(random(50, 650));
    parent1 = _parent1;
    parent2 = _parent2;
    gender = _gender;
  } // end Person method

  void display() {
    stroke(0);
    int i = 0;

    // loads circle diameters from hashtable, iterates thru all characteristics
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
      Object characteristic = e.nextElement();
      Object diameterCirc = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterCirc.toString());
      int diam = ceil(_diameter)*2;
      noStroke();
      ellipse(xPos+int(random(-40, 40)), yPos+int(random(-40, 40)), diam, diam);
      i++;
    }

    // displays main circle for each Person
    fill(0);
    ellipse(xPos, yPos, personNodeSize, personNodeSize); // overall rating, in middle
    text(namePerson, xPos+15, yPos+15);
  } // display for Person
  
} // end Person class
