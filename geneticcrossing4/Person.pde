class Person {
  Hashtable trait = new Hashtable();
  String namePerson, gender;
  int parent1, parent2, uniqueID, xPos, yPos, pWeight, pHeight, age, mbti, lastBaby, nationality, religion;
  int[] colorBaseArray = new int[3]; // need base colors for each person
  int[][] traitPosArray = new int[numCharacteristics][2]; // save x,y for all characteristics

  // constructor
  Person(int _uniqueID, int _mbti, int _gracefulness, int _luck, int _talentMath, int _talentArt, int _talentSports, int _strength,
  int _intelligence, int _wisdom, int _charisma, int _stamina, int _wit, int _humor, int _pWeight, int _pHeight,
  int _education, int _age, int _creativity, int _responsibility, int _discipline, int _honesty, int _religiosity,
  int _entrepreneurialism, int _appearance, int _money, String _namePerson, String _gender, int _parent1,
  int _parent2, int _nationality, int lastBaby, int _religion, int _xPos, int _yPos) {
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
    trait.put("luck", _luck);
    trait.put("math talent", _talentMath);
    trait.put("art talent", _talentArt);
    trait.put("sports talent", _talentSports);
    
    lastBaby = 0;
    uniqueID = _uniqueID;
    namePerson = _namePerson;
    parent1 = _parent1;
    parent2 = _parent2;
    gender = _gender;
    nationality = _nationality;
    religion = _religion;
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
      colorBaseArray[j] = int(random(255));
    }

    // generates random position for all the characteristics
    for (int i=0;i<numCharacteristics;i++) {
      traitPosArray[i][0] = xPos+int(random(-30, 30));
      traitPosArray[i][1] = yPos+int(random(-30, 30));
    }
    
  } // end Person method

  void display(int setOpacity) {

    // draws container circle
    noStroke();
    fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], int(setOpacity/2));
    ellipse(xPos, yPos, 70, 70);

    // loads circle diameters from hashtable, iterates thru all characteristics
    int i = 0;
    stroke(0);
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], setOpacity);
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
    fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], 255);
    ellipse(xPos, yPos, personNodeSize, personNodeSize); // overall rating, in middle
    textSize(8);
    int pFeet = round(pHeight / 12);
    int pInches = pHeight % 12;
    fill(255,200);
    text(namePerson + " (" + gender + ", " + age + "yo, " + getNationality(nationality) + ")" + "\n" + pWeight + "lbs, " + pFeet + "'" + pInches + "\"", xPos+15, yPos+15);

  } // display for Person
  
} // end Person class

