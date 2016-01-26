/**
 * Created by user on 26/01/2016.
 */
"use strict";

var loggedInUser;
var allBills;
var selectedBillsId;

var HTTP_METHODS = {
    GET: "GET",
    POST: "POST",
    PUT: "PUT"
};

//region Main
$(function() {
    console.log("Main script running...");

    $(window).on("navigate", function( event, data ) {
        console.log("Navigated to page");
    });

    $('a#joinBillButton').click(handleJoinBillClicked);

    $('form#loginForm').submit(function() {
        console.log("Login clicked");
        var user = new User($('form#loginForm').serializeObject());
        ajaxRequest(HTTP_METHODS.POST, "/rest/login", handleUserCreatedOrLogin, user);

        return false;
    });

    $('form#createUserForm').submit(function() {
        console.log("Create user clicked");
        var user = new User($('form#createUserForm').serializeObject());
        if (user.passwordsMatch()) {
            ajaxRequest(HTTP_METHODS.POST, "/rest/user", handleUserCreatedOrLogin, user);
        } else {
            error("Passwords dont match");
        }

        console.log("Trying to create user: " + user);
        return false;
    });
});
//endregion

//region Join Bill
function handleJoinBillClicked() {
    $.mobile.navigate("#joinBill");
    ajaxRequest(HTTP_METHODS.GET, "rest/bill", handleAllBills);
}

function handleAllBills(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        console.log(jsonResponse.content);
        var jsonBillList = jsonResponse.content;
        allBills = {};
        for (let jsonBill of jsonBillList) {
            var bill = new Bill(jsonBill);
            allBills[bill.id] = bill;
        }

        console.log(allBills);
        createListView("ul#allBills", selectOpenBills(allBills));
    }
}
//endregion

//region Display Bill
function displayBillWithId(id) {
    selectedBillsId = id;
    $.mobile.navigate("#displayBill");
    ajaxRequest(HTTP_METHODS.GET, "rest/bill/" + id + "/items", handleAllItems);
}

function handleAllItems(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        console.log(jsonResponse.content);
    }
}
//endregion

//region Bill
function Bill(jsonBill) {
    this.id = jsonBill.id;
    this.manager =  new User(jsonBill.manager);
    this.isPrivate = jsonBill.isPrivate;
    this.isOpen = jsonBill.isOpen;
    this.items = jsonBill.items;


    this.display = function() {
        return "ID: " + this.id + ", Created By: " + this.manager.username;
    }

    this.onClick = function() {
        return "displayBillWithId(" + this.id + ")";
    }
}
//endregion

//region User
function User(jsonUser) {
    this.username = jsonUser.username;
    this.password = jsonUser.password;
    this.confirmPassword = jsonUser.confirmPassword;
    this.id = jsonUser.id;

    this.passwordsMatch = function() {
        return (this.password === this.confirmPassword);
    }

    this.toJSON = function() {
        return {
            "username": this.username,
            "password": this.password
        };
    }
}

function handleUserCreatedOrLogin(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        console.log(jsonResponse.content);
        loggedInUser = new User(jsonResponse.content);
        $.mobile.navigate("#main", jsonResponse);
    }
}
//endregion

//region Utility
function ajaxRequest(method, url, callback, /*optional*/ entity, /*optional*/ errorCallback) {
    if (!errorCallback) {
        errorCallback = error;
    }

    $.ajax({
        url: url,
        method: method,
        data: entity ? JSON.stringify(entity.toJSON()) : "",
        dataType: 'json',
        contentType: "application/json",
        success: callback,
        error: errorCallback
    });
}

function createListView(listId, dataList) {
    $(listId).empty();
    $.each(dataList, function(index, data) {
        $(listId).append(
            '<li>'  +
            '<a href="" onclick=' + data.onClick() + '>' +
            '<h3>' + data.display() + '</h3>' +
            '</a>' +
            '</li>'
        );
    });	// end for each

    $(listId).listview('refresh');
}

function error(error) {
    console.log(error);
    $("#errorMsg").text(error);
    $.mobile.changePage("#error");
}

function selectOpenBills(billList) {
    var openBills = [];

    $.each(billList, function(key, bill) {
       if (bill.isOpen) {
           openBills.push(bill);
       }
    });

    return openBills;
}
//endregion