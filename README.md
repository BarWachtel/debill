Setup git (V)
Singelton MySQL db connection -

API (Rest (javax.ws.rs.*) OR Servlets (HttpServlets))
Only non-authorized paths -
	get /view/ - index (screen)
	get /facebook - setup user session

Authorized paths (redirect to index for non-authed users) -
	get /view/main - open bill, join bill, fuck bill
		get /view/openbill - camera interaction etc.
			post /bill/create - user needs to upload photo (content-type: image/jpg), returns bill id
			get /bill/{id}/items - returns all items
			get /bill/{id}/users - users connected to bill
		get /view/joinbill - show all available open bills
			get /bill/ - get all bills (those that belong to friends)
		get /view/bill - displays items & payment status
			post /bill/{id}/item/{id} - param. quantity
			post /bill/{id}/close - Only the user who opened the bill
		get /view/billsummary - who payed what

And more...

Logic & Redis -


Database -
User (DAO)
Bill (DAO)
Item (DAO)
User_to_Bill (link table)
Bill_to_Item (link table)
User_to_Item (link table)

DAO - Data Access Object