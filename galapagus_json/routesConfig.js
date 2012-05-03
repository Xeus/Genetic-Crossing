var jsonRoute = require('./routes/json');

module.exports = function(app) {
    app.get('/', jsonRoute.index);
    app.get('/json', jsonRoute.fetchJSON); // returns JSON of Galapagusers
    app.get('/add', jsonRoute.addUser); // reset MongoDB doc
    app.get('/import', jsonRoute.importUser);
    app.get('/jsonSingle', jsonRoute.jsonSingle);
}