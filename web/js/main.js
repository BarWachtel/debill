/**
 * Created by user on 26/01/2016.
 */
"use strict";

var loggedInUser;
var allBills = {};
var selectedBillsId;
var currentPageId;
var myShare = {};

var photoTaken;

function Persist() {
    var loggedInUser = undefined;
    var allBills = undefined;
    var selectedBillsId = undefined;
    var myShare = undefined;

    function getLoggedInUser() {
        if (!loggedInUser) {
            loggedInUser = new User(JSON.parse(localStorage.loggedInUser));
        }

        return loggedInUser;
    }

    function setLoggedInUser(_loggedInUser) {
        loggedInUser = _loggedInUser;
        localStorage.loggedInUser = JSON.stringify(loggedInUser);
    }

    function getAllBills() {
        if (!allBills) {
            allBills = Bill.fromJsonCollection(JSON.parse(localStorage.allBills));
        }

        return allBills;
    }

    function getBill(billId) {
        return getAllBills()[billId];
    }

    function setAllBills(_allBills) {
        allBills = _allBills;
        localStorage.allBills = JSON.stringify(_allBills);
    }

    function setBill(_bill) {
        allBills = getAllBills();
        allBills[_bill.id] = _bill;
        setAllBills(allBills);
    }

    function getSelectedBillsId() {
        if (!selectedBillsId) {
            selectedBillsId = Number(localStorage.selectedBillsId);
        }

        return selectedBillsId;
    }

    function setSelectedBillsId(_selectedBillsId) {
        selectedBillsId = _selectedBillsId;
        localStorage.selectedBillsId = selectedBillsId;
    }

    function getMyShare() {
        if (!myShare) {
            myShare = JSON.parse(localStorage.myShare);
        }

        return myShare[getSelectedBillsId()];
    }

    function addToMyShare(amount) {
        myShare[getSelectedBillsId()] += amount;
        localStorage.myShare = JSON.stringify(myShare);
    }
}

var HTTP_METHODS = {
    GET: "GET",
    POST: "POST",
    PUT: "PUT"
};

//region Main
function storeCurrentPageId() {
    currentPageId = $(".ui-page-active").attr('id');
}

$(function() {
    console.log("Main script running...");
    storeCurrentPageId();

    $(window).on("pageshow", function( event, data ) {
        storeCurrentPageId();
        if (currentPageId) {
            eval("pageLoad_" + currentPageId)();
        }
    });

    $(window).on("pagehide", function( event, data ) {
        console.log("Navigating away from page " + currentPageId);
    });

    $('a#joinBillButton').click(handleJoinBillClicked);

    $('button#uploadPhoto').click(uploadPhoto);

    $("#capture").on("change", tookPhoto);

    $('form#loginForm').submit(loginFormSubmitted);

    $('form#createUserForm').submit(createUserFormSubmitted);

    $('#closeBillButton').click(closeBillButtonClicked);
});
//endregion

//region On Page Load functions
function pageLoad_welcome() {
    console.log("On welcome page");
}

function pageLoad_main() {
    console.log("On main page");
}

function pageLoad_joinBill() {
    console.log("On join bill page");
    ajaxRequest(HTTP_METHODS.GET, "rest/bill", addAllBillsToListview);
}

function pageLoad_displayBill() {
    getSelectedBillsIdFromLocalStorage();
    console.log("On display bill page");
    if (!myShare[selectedBillsId]) {
        myShare[selectedBillsId] = 0;
    }

    if (allBills[selectedBillsId]) {
        displayBill();
    } else {
        ajaxRequest(HTTP_METHODS.GET, Bill.restGetPath(selectedBillsId), handleBillJsonRecieved);
    }
}

function pageLoad_createBill() {
    console.log("On create bill page");
}

function pageLoad_login() {
    // do nothing
}

function pageLoad_createUser() {
    // do nothing
}

function pageLoad_error() {
    // do nothing
}
//endregion

// Screens

//region Connection/Signup
function createUserFormSubmitted() {
    console.log("Create user clicked");
    var user = new User($('form#createUserForm').serializeObject());
    if (user.passwordsMatch()) {
        ajaxRequest(HTTP_METHODS.POST, "rest/user", handleUserCreatedOrLogin, user);
    } else {
        error("Passwords dont match");
    }

    console.log("Trying to create user: " + user);
    return false;
}

function loginFormSubmitted() {
    console.log("Login clicked");
    var user = new User($('form#loginForm').serializeObject());
    ajaxRequest(HTTP_METHODS.POST, "rest/login", handleUserCreatedOrLogin, user);

    return false;
}
//endregion

//region Join Bill
function handleJoinBillClicked() {
    $.mobile.navigate("#joinBill");
}

function addAllBillsToListview(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        for (let jsonBill of jsonResponse.content) {
            var bill = new Bill(jsonBill);
            allBills[bill.id] = bill;
        }
        createListView("ul#allBills", filterOpenBillsWithItems(allBills));
    }
}
//endregion

//region Display Bill
function joinBillAndDisplayItems(billId) {
    storeSelectedBillsId(billId);
    $.mobile.navigate("#displayBill");
}

function handleBillJsonRecieved(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        var bill = new Bill(jsonResponse.content);
        allBills[bill.id] = bill;
        displayBill();
    }
}

function displayTotalPricesAndMyShare(itemsCollection) {
    var total, payedFor;
    total = 0;
    payedFor = 0;

    $.each(itemsCollection, function(index, item) {
        total += item.total();
        payedFor += item.payedFor();
    });

    $("#totalSpan").text('$' + payedFor + ' / $' + total);
    $('#myShareSpan').text('$' + myShare[selectedBillsId]);

    if (payedFor == total) {
        $('#closeBillButton').removeAttr('disabled');
    }
}

function addAllItemsToListview(itemsCollection) {
    createListView("ul#billItemsListView", itemsCollection);
}

function displayBill() {
    displayTotalPricesAndMyShare(allBills[selectedBillsId].items);
    addAllItemsToListview(allBills[selectedBillsId].items);
}

function closeBillButtonClicked() {
    allBills[selectedBillsId].close();
}
//endregion


// Classes

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
    this.items = Item.fromJsonCollection(jsonBill.items);


    this.display = function() {
        return "ID: " + this.id + ", Created By: " + this.manager.username;
    };

    this.onClick = function() {
        return "joinBillAndDisplayItems(" + this.id + ")";
    };

    this.identifier = function() {
        return "__bill-" + this.id;
    };

    this.hasItems = function() {
        return Object.keys(this.items).length > 0;
    };

    this.close = function() {
        this.isOpen = false;
        updateResource(this);
    };

    this.restUpdatePath = function () {
        return "rest/bill/" + this.id;
    };

    this.restGetPath = function () {
        return "rest/bill/" + this.id;
    };

    this.updateView = function () {
        console.log("Bill: Update View");
    }

    this.toJSON = function() {
        return {
            id: this.id,
            isPrivate: this.isPrivate,
            isOpen: this.isOpen
        }
    }
}

Bill.restGetPath = function(id) {
    return "rest/bill/" + id;
}

Bill.fromJsonCollection = function (jsonCollection) {
    var billCollection = {};

    $.each(jsonCollection, function(key, jsonBill) {
        var bill = new Bill(jsonBill);
        billCollection[bill.id] = jsonBill;
    });

    return billCollection;
}
//endregion

//region Item
function Item(jsonItem) {
    this.billId = jsonItem.billId;
    this.id = jsonItem.id;
    this.name =  removeNonAlphanumeric(jsonItem.name);
    this.numPayedFor = jsonItem.numPayedFor;
    this.price = jsonItem.price;
    this.quantity = jsonItem.quantity;

    this.display = function() {
        return "(" + this.quantity + "/" + this.numPayedFor + ") " + this.name + ": $" + this.price;
    };

    this.onClick = function() {
        return "Item.doWasClicked(" + this.billId + "," + this.id + ")";
    };

    this.identifier = function() {
        return "__item-" + this.id;
    };

    this.wasClicked = function() {
        if (this.numPayedFor < this.quantity) {
            this.numPayedFor++;
            myShare[selectedBillsId] += this.price;
            updateResource(this);
        }
        console.log("Payed for " + this.numPayedFor + " " + this.name);
    };

    this.restUpdatePath = function() {
        return "rest/bill/" + this.billId + "/item/" + this.id;
    };

    this.updateView = function() {
        displayTotalPricesAndMyShare(allBills[selectedBillsId].items);
        $("#" + this.identifier()).text(this.display());
        $("ul#billItemsListView").listview('refresh');
    };

    this.total = function() {
        return this.price * this.quantity;
    };

    this.payedFor = function() {
        return this.price * this.numPayedFor;
    };

    this.toJSON = function() {
        return {
            billId: this.billId,
            id: this.id,
            name: this.name,
            quantity: this.quantity,
            price: this.price,
            numPayedFor: this.numPayedFor
        }
    }
}

Item.doWasClicked = function(billId, itemId) {
    allBills[billId].items[itemId].wasClicked();
};

Item.fromJsonCollection = function(jsonItemCollection) {
    var itemCollection = {};
    $.each(jsonItemCollection, function(key, jsonItem) {
        var item = new Item(jsonItem);
        itemCollection[item.id] = item;
    });

    return itemCollection;
};
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

function createListView(listViewId, dataCollection) {
    $(listViewId).empty();
    $.each(dataCollection, function(index, data) {
        $(listViewId).append(
            '<li>'  +
            '<a href="" onclick=' + data.onClick() + '>' +
            '<h3 id="' + data.identifier() + '">' + data.display() + '</h3>' +
            '</a>' +
            '</li>'
        );
    });	// end for each

    $(listViewId).listview('refresh');
}

function error(error) {
    console.log(error);
    $("#errorMsg").text(error);
    $.mobile.changePage("#error");
}

function filterOpenBillsWithItems(billList) {
    var openBills = [];

    $.each(billList, function(key, bill) {
        if (bill.isOpen && bill.hasItems()) {
            openBills.push(bill);
        }
    });

    return openBills;
}

function removeNonAlphanumeric(str) {
    return str.replace(/[^a-zA-Z 0-9]+/g, '');
}

function getSelectedBillsIdFromLocalStorage() {
    selectedBillsId = localStorage.selectedBillsId ? localStorage.selectedBillsId : 0;
}

function storeSelectedBillsId(billId) {
    selectedBillsId = billId;
    localStorage.selectedBillsId = selectedBillsId;
}
//endregion

//region Inform Server methods
function updateResource(resource) {
    ajaxRequest(
        HTTP_METHODS.PUT,
        resource.restUpdatePath(),
        function() { resource.updateView(); },
        resource);
}
//endregion

