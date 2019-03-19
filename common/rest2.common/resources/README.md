# Rest interface generator

## Generate files

You can use make to generator the Java classes. You need docker for this. See Makefile.

* `make generate`
* `make copy` copies API and model files. See Makefile.



## Changes in the generator templates

The openapi-generator-cli.jar contains Odysseus specific changes in the folder MSF4J (mustache templates).

If you want to upgrade the generator jar to a new version simple copy the jar from the docker container (see Makefile) and replace the templates in the MSF4J folder.

*Remark: Consider to improve this by adding an own generator module.*