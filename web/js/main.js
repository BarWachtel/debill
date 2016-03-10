/**
 * Created by user on 26/01/2016.
 */
"use strict";

// Global Variables

var currentPageId;
var photoTaken;
var pollForBillUpdates;

var HTTP_METHODS = {
    GET: "GET",
    POST: "POST",
    PUT: "PUT"
};
var SCREENS = {
    MAIN: '#main',
    WELCOME: '#welcome',
    JOIN_BILL: '#joinBill',
    DISPLAY_BILL: '#displayBill',
    BILL_CLOSED: '#billClosed'
}


//region Data Persistance

function Persist() {
    var loggedInUser = undefined;
    var allBills = undefined;
    var selectedBillsId = undefined;
    var myShare = undefined;

    this.getLoggedInUser = function () {
        if (!loggedInUser) {
            loggedInUser = localStorage.loggedInUser ?
                new User(JSON.parse(localStorage.loggedInUser)) : {};
        }

        return loggedInUser;
    }

    this.setLoggedInUser = function (_loggedInUser) {
        loggedInUser = _loggedInUser;
        localStorage.loggedInUser = JSON.stringify(loggedInUser);
    }

    this.getAllBills = function () {
        if (!allBills) {
            allBills = localStorage.allBills ?
                Bill.fromJsonCollection(JSON.parse(localStorage.allBills)) : {};
        }

        return allBills;
    }

    this.getCurrentBill = function () {
        return this.getBill(this.getSelectedBillsId());
    }

    this.getBill = function (billId) {
        return this.getAllBills()[billId];
    }

    this.setAllBills = function (_allBills) {
        allBills = _allBills;
        localStorage.allBills = JSON.stringify(_allBills);
    };

    this.setBill = function (_bill) {
        allBills = this.getAllBills();
        allBills[_bill.id] = _bill;
        this.setAllBills(allBills);
    }

    this.saveAllBills = function () {
        this.setAllBills(allBills);
    };

    this.getSelectedBillsId = function () {
        if (!selectedBillsId) {
            selectedBillsId = localStorage.selectedBillsId ?
                Number(localStorage.selectedBillsId) : -1;
        }

        return selectedBillsId;
    }

    this.setSelectedBillsId = function (_selectedBillsId) {
        selectedBillsId = _selectedBillsId;
        localStorage.selectedBillsId = selectedBillsId;
    }

    this.getMyShare = function () {
        if (!myShare) {
            myShare = localStorage.myShare ?
                JSON.parse(localStorage.myShare) : {};
        }

        if (!myShare[this.getSelectedBillsId()]) {
            myShare[this.getSelectedBillsId()] = 0;
        }

        return myShare[this.getSelectedBillsId()];
    }

    this.addToMyShare = function (amount) {
        if (!myShare[this.getSelectedBillsId()]) {
            myShare[this.getSelectedBillsId()] = 0;
        }
        myShare[this.getSelectedBillsId()] += amount;
        localStorage.myShare = JSON.stringify(myShare);
    }
}

var persist = new Persist();

//endregion

//region Main
function storeCurrentPageId() {
    currentPageId = $(".ui-page-active").attr('id');
}

$(function () {
    console.log("Main script running...");
    storeCurrentPageId();

    $(window).on("pageshow", function (event, data) {
        storeCurrentPageId();
        if (currentPageId) {
            eval("pageLoad_" + currentPageId)();
        }
    });

    $(window).on("pagehide", function (event, data) {
        console.log("Navigating away from page " + currentPageId);
        if (currentPageId === "displayBill") {
            if (pollForBillUpdates) {
                clearInterval(pollForBillUpdates);
                console.log("Stopped polling server for bill updates");
                pollForBillUpdates = null;
            }
        }
    });

    $('a#joinBillButton').click(handleJoinBillClicked);

    $('button#uploadPhoto').click(uploadPhoto);

    $("#capture").on("change", tookPhoto);

    $('form#loginForm').submit(loginFormSubmitted);

    $('form#createUserForm').submit(createUserFormSubmitted);

    $('#closeBillButton').click(closeBillButtonClicked);

    $('#logoutButton').click(logoutButtonClicked);
});
//endregion

//region On Page Load functions
function pageLoad_welcome() {
    console.log("On welcome page");
    var loggedInUser = persist.getLoggedInUser();
    if (loggedInUser.id) {
        console.log(loggedInUser.username + " already logged in");
        navigateToScreen(SCREENS.MAIN);
    }
}

function pageLoad_main() {
    console.log("On main page");
}

function pageLoad_joinBill() {
    console.log("On join bill page");
    ajaxRequest(HTTP_METHODS.GET, "rest/bill", addAllBillsToListview);
}

function pageLoad_displayBill() {
    console.log("On display bill page");
    if (persist.getCurrentBill()) {
        displayBill();
    } else {
        ajaxRequest(HTTP_METHODS.GET, Bill.restGetPath(persist.getSelectedBillsId()), handleBillJsonRecieved);
    }

    if (!pollForBillUpdates) {
        console.log("Starting to poll server for bill updates");
        pollForBillUpdates = setInterval(updateExistingBillDisplay, 400);
    }
}

function pageLoad_createBill() {
    console.log("On create bill page");
}

function pageLoad_billClosed() {
    console.log("On bill closed page");
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
        ajaxRequest(HTTP_METHODS.POST, "rest/user", handleUserCreatedOrLogin, user.toJSON());
    } else {
        error("Passwords don't match");
    }

    console.log("Trying to create user: " + user);
    return false;
}

function loginFormSubmitted() {
    console.log("Login clicked");
    var user = new User($('form#loginForm').serializeObject());
    ajaxRequest(HTTP_METHODS.POST, "rest/login", handleUserCreatedOrLogin, user.toJSON());

    return false;
}

function logoutButtonClicked() {
    localStorage.clear();
    persist = new Persist();
    console.log(persist.getLoggedInUser());
    navigateToScreen(SCREENS.WELCOME);
}
//endregion

//region Join Bill
function handleJoinBillClicked() {
    navigateToScreen(SCREENS.JOIN_BILL);
}

function addAllBillsToListview(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        for (let jsonBill of jsonResponse.content) {
            var bill = new Bill(jsonBill);
            persist.setBill(bill);
        }
        createListView("ul#allBills", filterOpenBillsWithItems(persist.getAllBills()));
    }
}
//endregion

//region Display Bill
function joinBillAndDisplayItems(billId) {
    persist.setSelectedBillsId(billId)
    navigateToScreen(SCREENS.DISPLAY_BILL);
}

function handleBillJsonRecieved(jsonResponse) {
    if (jsonResponse.error) {
        error(jsonResponse.errorMsg);
    } else {
        var bill = new Bill(jsonResponse.content);
        persist.setBill(bill);
        persist.setSelectedBillsId(bill.id);
        displayBill();
    }
}

function displayTotalPricesAndMyShare(itemsCollection) {
    var total, payedFor;
    total = 0;
    payedFor = 0;

    $.each(itemsCollection, function (index, item) {
        total += item.total();
        payedFor += item.payedFor();
    });

    $("#totalSpan").text('$' + payedFor + ' / $' + total);
    $('#myShareSpan').text('$' + persist.getMyShare());

    if (payedFor == total) {
        $('#closeBillButton').removeAttr('disabled');
    }
}

function addAllItemsToListview(itemsCollection) {
    createListView("ul#billItemsListView", itemsCollection);
}

function displayBill() {
    displayTotalPricesAndMyShare(persist.getCurrentBill().items);
    addAllItemsToListview(persist.getCurrentBill().items);
}

function closeBillButtonClicked() {
    persist.getCurrentBill().close();
    goToBillClosedScreen();
}

function goToBillClosedScreen() {
    $("#billClosedAmountToPay").text('$' + persist.getMyShare());
    navigateToScreen(SCREENS.BILL_CLOSED);
}

function updateExistingBillDisplay() {
    ajaxRequest(HTTP_METHODS.GET, Bill.restGetPath(persist.getSelectedBillsId()), function(jsonResponse) {
        if (jsonResponse.error) {
            error(jsonResponse.errorMsg);
        } else {
            var bill = new Bill(jsonResponse.content);
            persist.setBill(bill);
            if (!bill.isOpen) {
                goToBillClosedScreen();
            } else {
                $.each(bill.items, function (key, item) {
                    item.updateView();
                })
            }
        }
    });
}

//endregion


// Classes

//region User
function User(jsonUser) {
    this.username = jsonUser.username;
    this.password = jsonUser.password;
    this.confirmPassword = jsonUser.confirmPassword;
    this.id = jsonUser.id;

    this.passwordsMatch = function () {
        return (this.password === this.confirmPassword);
    }

    this.toJSON = function () {
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
        persist.setLoggedInUser(new User(jsonResponse.content));
        navigateToScreen(SCREENS.MAIN);
    }
}
//endregion

//region Bill
function Bill(jsonBill) {
    this.id = jsonBill.id;
    this.manager = new User(jsonBill.manager);
    this.isPrivate = jsonBill.isPrivate;
    this.isOpen = jsonBill.isOpen;
    this.items = Item.fromJsonCollection(jsonBill.items);


    this.display = function () {
        return "ID: " + this.id + ", Created By: " + this.manager.username;
    };

    this.onClick = function () {
        return "joinBillAndDisplayItems(" + this.id + ")";
    };

    this.identifier = function () {
        return "__bill-" + this.id;
    };

    this.hasItems = function () {
        return Object.keys(this.items).length > 0;
    };

    this.close = function () {
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

    this.toJSON = function () {
        return {
            id: this.id,
            isPrivate: this.isPrivate,
            isOpen: this.isOpen,
            manager: this.manager.toJSON(),
            items: JSON.stringify(this.items)
        }
    }

    this.updateJson = function () {
        var updateJson = this.toJSON();
        delete updateJson.items;
        delete updateJson.manager;
        return updateJson;
    }
}

Bill.restGetPath = function (id) {
    return "rest/bill/" + id;
}

Bill.fromJsonCollection = function (jsonCollection) {
    var billCollection = {};

    $.each(jsonCollection, function (key, jsonBill) {
        jsonBill.items = JSON.parse(jsonBill.items);
        var bill = new Bill(jsonBill);
        billCollection[bill.id] = bill;
    });

    return billCollection;
}
//endregion

//region Item
function Item(jsonItem) {
    this.billId = jsonItem.billId;
    this.id = jsonItem.id;
    this.name = removeNonAlphanumeric(jsonItem.name);
    this.numPayedFor = jsonItem.numPayedFor;
    this.price = jsonItem.price;
    this.quantity = jsonItem.quantity;

    this.display = function () {
        return "(" + this.quantity + "/" + this.numPayedFor + ") " + this.name + ": $" + this.price;
    };

    this.onClick = function () {
        return "Item.doWasClicked(" + this.billId + "," + this.id + ")";
    };

    this.identifier = function () {
        return "__item-" + this.id;
    };

    this.wasClicked = function () {
        if (this.numPayedFor < this.quantity) {
            this.numPayedFor++;
            persist.saveAllBills();
            persist.addToMyShare(this.price);
            updateResource(this);
        }
        console.log("Payed for " + this.numPayedFor + " " + this.name);
    };

    this.restUpdatePath = function () {
        return "rest/bill/" + this.billId + "/item/" + this.id;
    };

    this.updateView = function () {
        displayTotalPricesAndMyShare(persist.getCurrentBill().items);
        $("#" + this.identifier()).text(this.display());
        $("ul#billItemsListView").listview('refresh');
    };

    this.total = function () {
        return this.price * this.quantity;
    };

    this.payedFor = function () {
        return this.price * this.numPayedFor;
    };

    this.toJSON = function () {
        return {
            billId: this.billId,
            id: this.id,
            name: this.name,
            quantity: this.quantity,
            price: this.price,
            numPayedFor: this.numPayedFor
        }
    }

    this.updateJson = function () {
        return this.toJSON();
    }
}

Item.doWasClicked = function (billId, itemId) {
    persist.getBill(billId).items[itemId].wasClicked();
};

Item.fromJsonCollection = function (jsonItemCollection) {
    var itemCollection = {};
    $.each(jsonItemCollection, function (key, jsonItem) {
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
    if (event.target.files.length == 1 &&
        event.target.files[0].type.indexOf("image/") == 0) {
        $("#photoTaken").attr("src", URL.createObjectURL(event.target.files[0]));
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
        success: function (data, textStatus, jqXHR) {
            $.mobile.loading("hide");
            if (typeof data.error === 'undefined') {
                var bill = new Bill(data);
                persist.setSelectedBillsId(bill.id);
                persist.setBill(bill);
                navigateToScreen(SCREENS.DISPLAY_BILL);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.mobile.loading("hide");
            console.log('ERRORS: ' + textStatus);
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
        data: entity ? JSON.stringify(entity) : "",
        dataType: 'json',
        contentType: "application/json",
        success: callback,
        error: errorCallback
    });
}

function createListView(listViewId, dataCollection) {
    $(listViewId).empty();
    $.each(dataCollection, function (index, data) {
        $(listViewId).append(
            '<li>' +
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

    $.each(billList, function (key, bill) {
        if (bill.isOpen && bill.hasItems()) {
            openBills.push(bill);
        }
    });

    return openBills;
}

function removeNonAlphanumeric(str) {
    return str.replace(/[^a-zA-Z 0-9]+/g, '');
}

function navigateToScreen(screen) {
    $.mobile.navigate(screen);
};
//endregion

//region Inform Server methods
function updateResource(resource) {
    ajaxRequest(
        HTTP_METHODS.PUT,
        resource.restUpdatePath(),
        function () {
            resource.updateView();
        },
        resource.updateJson());
}
//endregion
