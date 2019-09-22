# Certificate Generator

Generates certificate for conferences and send it by email.

## Building

Run `mvn clean install`.

## Configuration

Make sure to setup the following properties:

| System Property                                | Type    | How to use                                                                                                         |
|------------------------------------------------|---------|--------------------------------------------------------------------------------------------------------------------|
| `certificate.storage.consumer.database.enable` | boolean | Enable certificate storage in the database                                                                         |
| `certificate.storage.consumer.email.enable`    | boolean | Automatic email when certificate is generated                                                                      |
| `certificate.email.subject`                    | text    | Email subject. Can use the following placeholders: **attendee.name**,* **conference.name** and **certificate.key** |
| `certificate.email.body`                       | text    | Email body. Can use the same placeholders as subject.                                                              |

The email and database configurations can be found in Quarkus documentation:

* [Quarkus email configuration](https://quarkus.io/guides/sending-emails)
* [Quarkus datasource configuration](https://quarkus.io/guides/datasource-guide)

In production you can set the following environment variables with the production values:

~~~
MYSQL_CERTIFICATES_JDBC_URL
MYSQL_USERNAME
MYSQL_PASSWORD
EMAIL_SENDER
STMP_HOST
STMP_SSL
STMP_USERNAME
STMP_PASSWORD
~~~

## Using

You can fetch conference data to this microservice using a CSV file. 
The CSV file location can be configured using the system property `certificate.fetcher.csv.file` and the CSV file should have the following format:

~~~
conference1 original ID,Conference1 name,Attendee1 Name,Attendee1 email,Attende1 attendance
conference1 original ID,Conference1 name,Attendee2 Name,Attendee2 email,Attende2 attendance
conference2 original ID,Conference2 name,Attendee1 Name,Attendee1 email,Attende1 attendance
~~~

Here's a line example:
~~~
1,"The big IT conf",Antonio Camara,antonio@email.com,true
~~~

The app works generating certificates based on a SVG file. The SVG file must have identified which field will hold the attendee name and the certificate id. 
This is represented by a CertificateModel, and to create a new Model you can PUT the following JSON to `/certificate-model`:

~~~
{
  "content": "string",
  "attendeeNameField": "string",
  "certificateKeyField": "string",
  "name": "string",
}
~~~

Where:
* **content**: The model SVG content;
* **attendeeNameField**: The field that will hold the attendee name
* **certificateKeyField**: the field that will hold the certificate key
* **name**: Some name you want to give to this certificate model

In later versions new fields can be supported (conference name, conference date, etc), but at this moment only attendee name and key is supported. 

The request will return the certificate model ID, save it.

**NOTE: MAKE SURE YOU ADD FIELDS WITH THE CONFIGURED IDS TO THE CSV, OTHERWISE THE APP WILL FAIL!**

Once you create the model you are able to finally generate certificates and send it by email. 
First you need to find the registration ids for the conference you want. Use the conference external ID:

~~~
/registration/external_conference_id/{externalConferenceId}
~~~

Then with the registration ID you can generate the certificate:

~~~
/certificate/model/{modelId}/registration/{registrationId}
~~~

Right now the app does not make batch generation and event sending, it is up to the client to send certification to each registration.

If you want to re-generate a certificate and do the whole process use the boolean query param force=true.
