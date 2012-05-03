var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// Define schema
var GalapaguserSchema = new Schema({
      name : String,
    	updated : { type: Date, default: Date.now },
    	strength : Number,
    	intelligence : Number,
    	wisdom : Number,
    	charisma : Number,
    	stamina : Number,
    	wit : Number,
    	humor : Number,
    	education : Number,
    	creativity : Number,
    	responsibility : Number,
    	discipline : Number,
    	honesty : Number,
    	religiosity : Number,
    	entrepreneurialism : Number,
    	appearance : Number,
    	money : Number,
    	gracefulness : Number,
    	stress : Number,
    	health : Number,
    	luck : Number,
    	talent_math : Number,
    	talent_art : Number,
    	talent_sports : Number,
});

module.exports = mongoose.model('Galapaguser', GalapaguserSchema);