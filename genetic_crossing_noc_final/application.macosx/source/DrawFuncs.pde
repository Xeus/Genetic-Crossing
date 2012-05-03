public class DrawFuncs {

  DrawFuncs() {
  }

  void drawPeople() {

    // only draw actual people, not empty objects
    for (int i=0;i<numPeople;i++) {
      int strength = (int)Float.parseFloat(person[i].trait.get("strength").toString());
      if (strength != 0) {
        if (person[i].alive == false && funeralsRecognized == true) {
        }
        else {
          person[i].display(50);
          drawFamilyLines(i, 25);
          drawFuncs.drawHappiness(i);
        }
        /* person[i].x = person[i].x + calcCultureMoveX(i, person[i].x);
         int finalY = calcCultureMoveY(i, person[i].y);
         int speedY = ceil(abs(person[i].y - finalY) / 20);
         if (person[i].y < finalY) person[i].y = person[i].y + speedY;
         */
      }
    }
    // show God
    god.display(50);
  }

  void drawFamilyLines(int i, int lineOpacity) { // draw familial connection lines

    stroke(255, 255, 255, lineOpacity);
    line(god.x, god.y, person[i].x, person[i].y);

    if (person[i].parent1 != -1) {
      stroke(0, 0, 255, lineOpacity);
      int midPointParent1X = round((person[person[i].parent1].x + person[i].x) / 2);
      int midPointParent1Y = round((person[person[i].parent1].y + person[i].y) / 2);
      int midPointParent2X = round((person[person[i].parent2].x + person[i].x) / 2);
      int midPointParent2Y = round((person[person[i].parent2].y + person[i].y) / 2);
      line(person[person[i].parent1].x, person[person[i].parent1].y, person[i].x, person[i].y);
      line(person[person[i].parent2].x, person[person[i].parent2].y, person[i].x, person[i].y);
      stroke(0, 255, 0, lineOpacity);
      fill(0, 0, 255, 255);
      text(Integer.toString(person[i].parent1RL), midPointParent1X, midPointParent1Y);
      text(Integer.toString(person[i].parent2RL), midPointParent2X, midPointParent2Y);
      line(person[person[i].parent1].x, person[person[i].parent1].y, person[person[i].parent2].x, person[person[i].parent2].y);
    }

    // nation connection lines
    stroke(255, 255, 0, lineOpacity);
    line(person[i].x, person[i].y, nation[person[i].nationality].x, nation[person[i].nationality].y);
    fill(255, 255, 0, 255);
    int midPointNationX = round((nation[person[i].nationality].x + person[i].x) / 2);
    int midPointNationY = round((nation[person[i].nationality].y + person[i].y) / 2);
    text(Integer.toString(person[i].nationRL), midPointNationX, midPointNationY);

    // religion connection lines
    stroke(3, 255, 181, lineOpacity);
    line(person[i].x, person[i].y, religion[person[i].religion].x, religion[person[i].religion].y);
    fill(0, 255, 255, 255);
    int midPointReligionX = round((religion[person[i].religion].x + person[i].x) / 2);
    int midPointReligionY = round((religion[person[i].religion].y + person[i].y) / 2);
    text(Integer.toString(person[i].religionRL), midPointReligionX, midPointReligionY);
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
    String actualTrait = "";
    String[] mbti = new String[2];

    for (int i=0;i<numPeople;i++) {
      int strength = (int)Float.parseFloat(person[i].trait.get("strength").toString());
      if (strength != 0) {
        if (mouseX >= person[i].x-(int(personNodeSize/2)) && mouseX <= person[i].x+(int(personNodeSize/2)) && mouseY >= person[i].y-(int(personNodeSize/2)) && mouseY <= person[i].y+(int(personNodeSize/2))) {
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
              int potentialTrait = int(_characteristic);
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
  void drawPopUpBoxNations() {
    for (int i=0;i<numNations;i++) {
      int strength = (int)Float.parseFloat(nation[i].trait.get("security").toString());
      if (strength != 0) {
        if ((mouseX >= nation[i].x-int(nationBoxSize/2)) && (mouseX <= nation[i].x+int(nationBoxSize/2)) && (mouseY >= nation[i].y-int(nationBoxSize/2)) && (mouseY <= nation[i].y+int(nationBoxSize/2))) {
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
  void drawPopUpBoxReligions() {
    for (int i=0;i<numReligions;i++) {
      int commercial = (int)Float.parseFloat(religion[i].trait.get("commercial").toString());
      if (commercial != 0) {
        if ((mouseX >= religion[i].x) && (mouseX <= religion[i].x+religionBoxSize) && (mouseY >= religion[i].y) && (mouseY <= religion[i].y+religionBoxSize)) {
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
    text("'u' stops physics, 'space' stops sketch, blue = parent -> child, green = parents, yellow = nationality, turquoise = religion", 15, 15);

    // buttons
    noStroke();

    // chromosomal stainings/time universe
    if ((mouseX >= buttonX) && (mouseX <= buttonX+buttonSizeX) && (mouseY >= buttonY) && (mouseY <= buttonY+buttonSizeY)) {
      fill(#FFD95D);
    }
    else {
      fill(#FFC400);
    }
    rect(buttonX, buttonY, buttonSizeX, buttonSizeY);

    textSize(10);
    fill(0);
    text("stats & chromosomal stainings", buttonX+4, buttonY+15);

    // make font bigger
    if ((mouseX >= buttonX + 155) && (mouseX <= buttonX+175) && (mouseY >= buttonY) && (mouseY <= buttonY + 25)) {
      fill(#FFD95D);
    }
    else {
      fill(#FFC400);
    }
    rect(buttonX + 155, buttonY, 20, 25);

    textSize(10);
    fill(0);
    text("f", buttonX+163, buttonY+15);

    // make font smaller
    if ((mouseX >= buttonX + 180) && (mouseX <= buttonX+200) && (mouseY >= buttonY) && (mouseY <= buttonY + 25)) {
      fill(#FFD95D);
    }
    else {
      fill(#FFC400);
    }
    rect(buttonX + 180, buttonY, 20, 25);

    textSize(10);
    fill(0);
    text("F", buttonX+188, buttonY+15);
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
    text("view genetic/time map", buttonX+8, buttonY+15);
    int i = 0; // iterator for which characteristic to print for the legend
    int j = 20; // distance between each item in the legend
    for (int h=0;h<numPeople;h++) {
      for (Enumeration e = person[h].trait.keys() ; e.hasMoreElements() ;) {
        Object characteristic = e.nextElement();
        Object diameterCirc = person[h].trait.get(characteristic);
        float _diameter = Float.parseFloat(diameterCirc.toString());
        if (person[h].alive) {
          fill(colorArray[i][0], colorArray[i][1], colorArray[i][2], 100);
        }
        else {
          fill(100, 150);
        }
        ellipse(width/2 + 120 + i * 20, karyogramPush + 120 + j * h, _diameter * 3, _diameter * 3);
        fill(colorArray[i][0], colorArray[i][1], colorArray[i][2]);
        i++;
      }
      textSize(14);
      fill(255);
      textAlign(RIGHT);
      text(person[h].namePerson, width/2+50, karyogramPush + 125 + j * h);
      textAlign(BASELINE);
      fill(255);
      textSize(10);
      rect(caButtonX, karyogramPush + 110 + j * h, caButtonSize, caButtonSize);
      fill(0);
      text("CA", width/2+77, karyogramPush + 123 + j * h);
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
      text(characteristic.toString(), 300, 4+l * j);
      l++;
    }
    popMatrix();

    // push karyograms down
    if ((mouseX >= karyogramButtonDownX) && (mouseX <= karyogramButtonDownX + karyogramButtonSize) && (mouseY >= karyogramButtonDownY) && (mouseY <= karyogramButtonDownY + karyogramButtonSize)) {
      fill(#FFD95D);
    }
    else {
      fill(#FFC400);
    }
    rect(karyogramButtonUpX, karyogramButtonUpY, karyogramButtonSize, karyogramButtonSize);

    textSize(10);
    fill(0);
    text("up", karyogramButtonUpX+8, karyogramButtonUpY+17);

    // push karyograms up
    if ((mouseX >= karyogramButtonUpX) && (mouseX <= karyogramButtonUpX + karyogramButtonSize) && (mouseY >= karyogramButtonUpY) && (mouseY <= karyogramButtonUpY + karyogramButtonSize)) {
      fill(#FFD95D);
    }
    else {
      fill(#FFC400);
    }
    rect(karyogramButtonDownX, karyogramButtonDownY, karyogramButtonSize, karyogramButtonSize);

    textSize(10);
    fill(0);
    text("down", karyogramButtonDownX+3, karyogramButtonDownY+17);
  }

  void drawStats() {
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
      avgAge = int(avgAgeTotal / livePeople);
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
    int personCharsAvg = int(personCharsRawTotal[highestCharsId] / numCharacteristics);
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
    float ratioPotential = 0.0;

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
      if (personCharsRealTotal == 0) { 
        personCharsRealTotal = 1;
      }
      if (personCharsIdealTotal == 0) { 
        personCharsIdealTotal = 1;
      }
      ratioPotential = float(personCharsRealTotal) / float(personCharsIdealTotal);

      text(nation[i].nameNation, 200, yLine);
      yLine += 15;
      text(" population: " + populationNation[i], 200, yLine);
      yLine += 15;
      text(" raw well-being: " + personCharsNationRaw, 200, yLine);
      yLine += 15;
      text(" actual/potential well-being ratio: " + nf(ratioPotential, 0, 2), 200, yLine);
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
  void drawHappiness(int i) {
    int happiness = evolutions.happiness(i);
    String happinessText;
    color happinessColor = #FFFFFF;
    if (happiness < 20) {
      happinessColor = #FF0000;
      happinessText = "very unhappy";
    }
    else if (happiness < 40) {
      happinessColor = #FF7F24;
      happinessText = "unhappy";
    }
    else if (happiness < 60) {
      happinessColor = #FFE303;
      happinessText = "content";
    }
    else {
      happinessColor = #99CC32;
      happinessText = "happy";
    }
    fill(happinessColor, 200);
    text(happinessText, person[i].x+15, person[i].y+34);
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
   totalMoveX = nation[i].x - personX;
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
   totalMoveY = (nation[i].y + personY) / 2;
   }
   }
   return totalMoveY;
   }
   */
}

