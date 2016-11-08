
## @knitr setup, echo=FALSE
options(width = 75)
options(useFancyQuotes=FALSE)


## @knitr hook-printfun, echo=FALSE
library(knitr)
library(formatR)
knit_hooks$set(printfun = function(before, options, envir) {
  if (before) return()
  txt = capture.output(dump(options$printfun, '', envir = envir))
  ## reformat if tidy=TRUE
  if (options$tidy)
  	txt = tidy.source(text=txt, output=FALSE,
  		width.cutoff=30L, keep.comment=TRUE,
  		keep.blank.line=FALSE)$text.tidy
  paste(c('\n```r\n', txt, '\n```\n'), collapse="\n")
})


## @knitr hook-printmanpage, echo=FALSE
knit_hooks$set(printmanpage = function(before, options, envir) {
  if (before) return()
    manpage <- file.path("..", "man",
      sprintf("%s.Rd", options$printmanpage))
  lines <- readLines(manpage)
  ret <- "\n"
  for (line in lines)
  {
      ret <- paste(ret, "\t", line, "\n", sep="")
  }
  ret
})


## @knitr library_RGalaxy_fake, eval=FALSE
## library(RGalaxy)


## @knitr library_RGalaxy_real, echo=FALSE, results="hide"
suppressPackageStartupMessages(library(RGalaxy))


## @knitr addTwoNumbers, printfun='addTwoNumbers', echo=FALSE, tidy=FALSE
#source code goes here


## @knitr run_addTwoNumbers
t <- tempfile()
addTwoNumbers(2, 2, t)
readLines(t, warn=FALSE)


## @knitr addTwoNumbers_man, printmanpage='addTwoNumbers', echo=FALSE, tidy=FALSE
#source code goes here


## @knitr galaxyHomeSetup, echo=FALSE, results="hide"
if (!exists("galaxyHome"))
  galaxyHome <- getwd()
toolDir <- "RGalaxy_test_tool"
funcName <- "functionToGalaxify"

file.copy(system.file("galaxy", "tool_conf.xml", package="RGalaxy"),
    file.path(galaxyHome, "tool_conf.xml"), overwrite=FALSE)
 if(!file.exists("test-data")) dir.create("test-data", )


## @knitr run_galaxy, tidy=FALSE
galaxy("addTwoNumbers",
    galaxyConfig=
      GalaxyConfig(galaxyHome, "mytool", "Test Section",
        "testSectionId")
    )


## @knitr addTwoNumbersWithTest, printfun='addTwoNumbersWithTest', echo=FALSE, tidy=FALSE
#source code goes here


## @knitr runFunctionalTest
runFunctionalTest(addTwoNumbersWithTest)


## @knitr withRserve, tidy=FALSE, eval=FALSE
## galaxy("addTwoNumbersWithTest",
##     galaxyConfig=
##       GalaxyConfig(galaxyHome, "mytool", "Test Section",
##         "testSectionId"),
##     RserveConnection=RserveConnection()
##     )


## @knitr install_RSclient, eval=FALSE
## source("http://bioconductor.org/biocLite.R")
## biocLite("RSclient", siteRepos="http://www.rforge.net")


## @knitr RserveConnection
RserveConnection(host="mymachine", port=2012L)


## @knitr probeLookup, printfun='probeLookup', echo=FALSE, tidy=FALSE
#source code goes here


## @knitr test_probeLookup
runFunctionalTest(probeLookup)


