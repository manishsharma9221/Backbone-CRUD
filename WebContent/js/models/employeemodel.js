window.Employee = Backbone.Model.extend({
	urlRoot: "api/employees",
	defaults: {
		"id": null,
	    "name":  "",
	    "grade":  "",
	    "country":  "India",
	    "region":  "Indore",
	    "year":  "",
	    "description":  ""
	   
	  }
});

window.EmployeeCollection = Backbone.Collection.extend({
	model: Employee,
	url: "api/employees"
});