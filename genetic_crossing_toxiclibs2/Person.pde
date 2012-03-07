class Person extends VerletParticle2D {
  Hashtable trait = new Hashtable();
  String namePerson, gender;
  int parent1, parent2, uniqueID, xPos, yPos, pWeight, pHeight, age, mbti, lastBaby,
  nationality, religion, children,strength,intelligence,wisdom,charisma,stamina,wit,
  humor,education,creativity,responsibility,discipline,honesty,religiosity,entrepreneurialism,
  appearance,money,gracefulness,stress,health,luck,talentMath,talentArt,talentSports, happiness, employed;
  int[] colorBaseArray = new int[3]; // need base colors for each person
  int[][] traitPosArray = new int[numCharacteristics][2]; // save x,y for all characteristics
  int[][] traitPosArray_orig = new int[numCharacteristics][2]; // original copy of coords
  boolean alive;

  // constructor
  Person(int _uniqueID, int _mbti, int _stress, int _health, int _gracefulness, int _luck, int _talentMath, int _talentArt, int _talentSports, int _strength,
  int _intelligence, int _wisdom, int _charisma, int _stamina, int _wit, int _humor, int _pWeight, int _pHeight,
  int _education, int _age, int _creativity, int _responsibility, int _discipline, int _honesty, int _religiosity,
  int _entrepreneurialism, int _appearance, int _money, String _namePerson, String _gender, int _parent1,
  int _parent2, int _nationality, int lastBaby, int _religion, int _children, int _employed, int _xPos, int _yPos) {
    
    super(_xPos, _yPos);
    xPos = _xPos;
    yPos = _yPos;
    
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

    // each Person object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = int(random(255));
    }

    // generates random position for all the characteristics
    for (int i=0;i<numCharacteristics;i++) {
      traitPosArray_orig[i][0] = int(random(-30, 30));
      traitPosArray_orig[i][1] = int(random(-30, 30));
    }
    
  } // end Person method

  void display(int setOpacity) {
    
    // draws container circle
    noStroke();
    if (alive == true) {
      fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], int(setOpacity/2));
    }
    else {
      fill(#BCBCBC, setOpacity);
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
        fill(#BCBCBC, setOpacity);
      }
      Object characteristic = e.nextElement();
      Object diameterCirc = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterCirc.toString());
      int diam = ceil(_diameter)*2;
      noStroke();
      
      // generates random position for all the characteristics
    for (int j=0;j<numCharacteristics;j++) {
      traitPosArray[j][0] = traitPosArray_orig[j][0] + xPos;
      traitPosArray[j][1] = traitPosArray_orig[j][1] + yPos;
    }

      ellipse(traitPosArray[i][0], traitPosArray[i][1], diam, diam);
      i++;
    }

    // displays main circle for each Person
    stroke(255);
    if (alive == true) {
      fill(colorBaseArray[0], colorBaseArray[1], colorBaseArray[2], 255);
    }
    else {
      fill(#BCBCBC, setOpacity);
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
