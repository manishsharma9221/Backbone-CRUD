window.EmployeeView = Backbone.View.extend({

    tagName: "div", // Not required since 'div' is the default if no el or tagName specified

    initialize: function() {
        this.template = _.template(tpl.get('employee-details'));
		this.model.bind("change", this.render, this);
    },

    render: function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
    },

    events: {
        "change input": "change",
		"click .save": "saveEmployee",
		"click .delete": "deleteEmployee"
    },

    change: function(event) {
        var target = event.target;
        console.log('changing ' + target.id + ' from: ' + target.defaultValue + ' to: ' + target.value);
		// You could change your model on the spot, like this:
        // var change = {};
        // change[target.name] = target.value;
        // this.model.set(change);
    },

	saveEmployee: function() {
		this.model.set({
			name: $('#name').val(),
			grade: $('#grade').val(),
			country: $('#country').val(),
			region: $('#region').val(),
			year: $('#year').val(),
			description: $('#description').val()
		});
		if (this.model.isNew()) {
			var self = this;
			app.employeeList.create(this.model, {
				success: function() {
					app.navigate('employees/'+self.model.id, false);
				}
			});
		} else {
			this.model.save();
		}

		return false;
	},

	deleteEmployee: function() {
		this.model.destroy({
			success: function() {
				alert('Employee deleted successfully');
				window.history.back();
			}
		});
		return false;
	}

});