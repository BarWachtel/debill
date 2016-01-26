/**
 * Created by user on 26/01/2016.
 */
"use strict";

var loggedInUser;
var allBills = {};
var selectedBillsId;
var photoTaken;

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

    $('button#uploadPhoto').click(uploadPhoto);

    $("#capture").on("change", tookPhoto);

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

// Screens

//region Join Bill
function handleJoinBillClicked() {
    $.mobile.navigate("#joinBill");
    ajaxRequest(HTTP_METHODS.GET, "rest/bill", addAllBillsToListview);
}

function addAllBillsToListview(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        for (let jsonBill of jsonResponse.content) {
            var bill = new Bill(jsonBill);
            allBills[bill.id] = bill;
        }
        createListView("ul#allBills", filterOnlyOpenBills(allBills));
    }
}
//endregion

//region Display Bill
function joinBillAndDisplayItems(id) {
    selectedBillsId = id;
    $.mobile.navigate("#displayBill");
    ajaxRequest(HTTP_METHODS.GET, "rest/bill/" + id, handleBillJsonRecieved);
}

function handleBillJsonRecieved(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        displayBill(jsonResponse.content);
    }
}

var addAllItemsToListview = function (itemsList) {
    createListView("ul#billItemsListView", itemsList);
}

function displayBill(data) {
    var bill = new Bill(data);
    allBills[bill.id] = bill;
    selectedBillsId = bill.id;
    $.mobile.navigate("#displayBill");
    addAllItemsToListview(bill.items);
}
//endregion

// Utility Methods

//region Photo
function actualTakePhoto() {
    $("#capture").click();
}

function tookPhoto(event) {
    if(event.target.files.length == 1 &&
        event.target.files[0].type.indexOf("image/") == 0) {
        $("#photoTaken").attr("src",URL.createObjectURL(event.target.files[0]));
        photoTaken = event.target.files[0];
        $("#uploadPhoto").attr("disabled", false);
    }
}

function uploadPhoto() {
    var data = new FormData();
    data.append("file", photoTaken);

    $.mobile.loading("show", {
        text: "Creating Bill",
        textVisible: true,
        theme: "b",
        textonly: false
    });

    $.ajax({
        url: 'rest/files/upload',
        type: 'POST',
        data: data,
        cache: false,
        dataType: 'json',
        processData: false, // Don't process the files
        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
        success: function(data, textStatus, jqXHR)
        {
            if(typeof data.error === 'undefined')
            {
                // Success so call function to process the form
                console.log("Success!");
                $.mobile.loading("hide");
                displayBill(data);
            }
            else
            {
                // Handle errors here
                console.log('ERRORS: ' + data.error);
            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
            // STOP LOADING SPINNER
        }
    });
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

function filterOnlyOpenBills(billList) {
    var openBills = [];

    $.each(billList, function(key, bill) {
       if (bill.isOpen) {
           openBills.push(bill);
       }
    });

    return openBills;
}
//endregion

// Objects

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

//region Bill
function Bill(jsonBill) {
    this.id = jsonBill.id;
    this.manager =  new User(jsonBill.manager);
    this.isPrivate = jsonBill.isPrivate;
    this.isOpen = jsonBill.isOpen;
    this.items = Item.fromJsonList(jsonBill.items);


    this.display = function() {
        return "ID: " + this.id + ", Created By: " + this.manager.username;
    }

    this.onClick = function() {
        return "joinBillAndDisplayItems(" + this.id + ")";
    }
}
//endregion

//region Item
function Item(jsonItem) {
    this.billId = jsonItem.billId;
    this.id = jsonItem.id;
    this.name = jsonItem.name;
    this.numPayedFor = jsonItem.numPayedFor;
    this.price = jsonItem.price;
    this.quantity = jsonItem.quantity;

    this.display = function() {
        return "(" + this.quantity + "/" + this.numPayedFor + ") " + this.name + ": $" + this.price;
    };

    this.onClick = function() {
        return "Item.doWasClicked(" + this.billId + "," + this.id + ")";
    };

    this.wasClicked = function() {
        if (this.numPayedFor < this.quantity) {
            this.numPayedFor++;
        }
        console.log("Payed for " + this.numPayedFor + " " + this.name);
    }
}

Item.doWasClicked = function(billId, itemId) {
    allBills[billId].items[itemId].wasClicked();
};

Item.fromJsonList = function(jsonItemList) {
    var itemList = {};
    $.each(jsonItemList, function(key, jsonItem) {
        var item = new Item(jsonItem);
        itemList[item.id] = item;
    });

    return itemList;
};
//endregion