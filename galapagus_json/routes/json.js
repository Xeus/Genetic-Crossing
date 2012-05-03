// Module dependencies.
var db = require('../accessDB')
  , format = require('util').format;

module.exports = {

    index: function(request, response) {
        response.render('index.html');
    },

    fetchJSON: function(request, response) {

        /*
            HEY! LISTEN!  Here's the PHP/MySQL equivalent:
            $sth = mysql_query("SELECT ...");
            $rows = array();
            while($r = mysql_fetch_assoc($sth)) {
                $rows[] = $r;
            }
            print json_encode($rows);

        */

        // define the fields you want to include in your json data
        includeFields = ['_id','name','updated','strength','intelligence','wisdom','charisma','stamina', 'wit', 'humor', 'education', 'creativity', 'responsibility', 'discipline', 'honesty', 'religiosity', 'entrepreneurialism', 'appearance', 'money', 'gracefulness', 'stress', 'health', 'luck', 'talent_math', 'talent_art', 'talent_sports'];

        // query for all blog
        queryConditions = {}; //empty conditions - return everything
        var query = db.Galapaguser.find( queryConditions, includeFields);

        query.exec(function (err, galapagusers) {

            // render the card_form template with the data above
            jsonData = {
              'status' : 'OK',
              'JSONtitle' : 'All Galapagusers',
              'galapagusers' : galapagusers
            };

            response.json(jsonData);
        });
    },
    
    addUser : function(request, response) {
        
        // makes sure a test user exists

        var userData = {
            name : "Added User",
            strength : Math.ceil(Math.random() * 10),
            intelligence : Math.ceil(Math.random() * 10),
            wisdom : Math.ceil(Math.random() * 10),
            charisma : Math.ceil(Math.random() * 10),
            stamina : Math.ceil(Math.random() * 10),
            wit : Math.ceil(Math.random() * 10),
            humor : Math.ceil(Math.random() * 10),
            education : Math.ceil(Math.random() * 10),
            creativity : Math.ceil(Math.random() * 10),
            responsibility : Math.ceil(Math.random() * 10),
            discipline : Math.ceil(Math.random() * 10),
            honesty : Math.ceil(Math.random() * 10),
            religiosity : Math.ceil(Math.random() * 10),
            entrepreneurialism : Math.ceil(Math.random() * 10),
            appearance : Math.ceil(Math.random() * 10),
            money : Math.ceil(Math.random() * 10),
            gracefulness : Math.ceil(Math.random() * 10),
            stress : Math.ceil(Math.random() * 10),
            health : Math.ceil(Math.random() * 10),
            luck : Math.ceil(Math.random() * 10),
            talent_math : Math.ceil(Math.random() * 10),
            talent_art : Math.ceil(Math.random() * 10),
            talent_sports : Math.ceil(Math.random() * 10),
        };
        var newGalapaguser = new db.Galapaguser(userData);
        newGalapaguser.save();

        response.redirect('/json');
    },

    importUser: function(request, response) {

        var conditions = { userID : 0 }
        var options = { upsert: true, multi: false };

        var userData = {
            name : request.query.namePerson,
            strength : request.query.strength,
            intelligence : request.query.intelligence,
            wisdom : request.query.wisdom,
            charisma : request.query.charisma,
            stamina : request.query.stamina,
            wit : request.query.wit,
            humor : request.query.humor,
            education : request.query.education,
            creativity : request.query.creativity,
            responsibility : request.query.responsibility,
            discipline : request.query.discipline,
            honesty : request.query.honesty,
            religiosity : request.query.religiosity,
            entrepreneurialism : request.query.entrepreneurialism,
            appearance : request.query.appearance,
            money : request.query.money,
            gracefulness : request.query.gracefulness,
            stress : request.query.stress,
            health : request.query.health,
            luck : request.query.luck,
            talent_math : request.query.talent_math,
            talent_art : request.query.talent_art,
            talent_sports : request.query.talent_sports
        };

        db.ImportedUser.update(conditions, userData, options, function(err, numAffected) {
            if (err) {
                console.error("an error occurred");
                console.error(err);
            }
            
            if (numAffected == 0) {
                var userData = {
                    name : request.query.namePerson,
                    userID : 0,
                    strength : request.query.strength,
                    intelligence : request.query.intelligence,
                    wisdom : request.query.wisdom,
                    charisma : request.query.charisma,
                    stamina : request.query.stamina,
                    wit : request.query.wit,
                    humor : request.query.humor,
                    education : request.query.education,
                    creativity : request.query.creativity,
                    responsibility : request.query.responsibility,
                    discipline : request.query.discipline,
                    honesty : request.query.honesty,
                    religiosity : request.query.religiosity,
                    entrepreneurialism : request.query.entrepreneurialism,
                    appearance : request.query.appearance,
                    money : request.query.money,
                    gracefulness : request.query.gracefulness,
                    stress : request.query.stress,
                    health : request.query.health,
                    luck : request.query.luck,
                    talent_math : request.query.talent_math,
                    talent_art : request.query.talent_art,
                    talent_sports : request.query.talent_sports
                };

                var newGalapaguser = new db.Galapaguser(userData);
                newGalapaguser.save();
            }
        });

        // render the card_form template with the data above
        jsonData = {
            'status' : 'OK'
        };

        response.json(jsonData);
    },

    jsonSingle: function(request, response) {

        // define the fields you want to include in your json data
        includeFields = ['_id','userID', 'name','updated','strength','intelligence','wisdom','charisma','stamina', 'wit', 'humor', 'education', 'creativity', 'responsibility', 'discipline', 'honesty', 'religiosity', 'entrepreneurialism', 'appearance', 'money', 'gracefulness', 'stress', 'health', 'luck', 'talent_math', 'talent_art', 'talent_sports'];

        // query for all blog
        queryConditions = { userID : 0 }; //empty conditions - return everything
        var query = db.ImportedUser.find( queryConditions, includeFields);

        query.exec(function (err, importedUser) {

            // render the card_form template with the data above
            jsonData = {
              'status' : 'OK',
              'JSONtitle' : 'Imported Galapaguser',
              'importedUser' : importedUser
            };

            response.json(jsonData);
        });
    }
    
}