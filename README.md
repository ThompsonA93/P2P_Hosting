# VerSys_P2PProjekt
> 3 Subproject
> Structural P2P
> TCP-Protocl

## Message Format
@Request  :: Command 			/ Request or Offer
	  :: [Ressource]@[Address] 	/ What is requested/offered

Example:
	String s = Command@picture.png@IP:Port

@Response :: [List<String>]		/ Data returned by SuperServer 	
Example:
	
@Command :: [Abstract Object over List of Commands]
	AddRessource();
	GetRessource();
	DetachRessouce();
	ListRessources();
	Serve();		

## SuperServer
DHT-Distributer, notwendig als /root, shutdown => Aus

DHT.list
> Key:Value(=Object)

```
-> receiveRequest(Request r);
-->> addRessource();	// Seeder meldet an
	updateDHT();	
-->> getRessource();	// Lecher Request specific file
	fetchFromDHT();
-->> detachRessource;	// Seeder meldet ab
	updateDHT();
-->> listRessources();	// Leecher request offered files
	listDHTRessouces();
```


## LeecherClient
Nur Requests/Downloads/Beziehen von Ressourcen

```
-> sendRequest(Request r);
	receive();
```

## SeederClient
Server der sich beim SuperServer anmeldet mit
* Addresse
* Ressourcen

```
-> sendRequest(Request r);
	serve();
```
