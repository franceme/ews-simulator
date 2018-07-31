# Encryption Web Services Simulator
Encryption Web Services (EWS) provides a SOAP-based web service for Vantiv’s Encryption Services. This includes primary support for OmniToken and in some case, legacy support for Reverse Crypto. Operations that support Reverse Crypto will be noted. A caveat to this support is that the service assumes that Reverse Crypto at Vantiv will be retired prior to the next key rotation; hence supported operations will not return the token Id at this time.

This document details the use of Vantiv’s Encryption Web Services and its message structure. The intended audience is developers who want to consume Vantiv’s Encryption Web Services and have received the on-boarding materials that are associated with this document.
In the Message Structure section, the entire message is broken down layer by layer and is meant as a reference. Many of the tags seen in that section are not meant to be used together, but rather describe each tag and its potential inner tags and attributes. This is a great place to find the meaning of a tag and how to format the contents properly.

The Web Service Operations section calls out each operation and relevant details about the unique fields. 
An important note about the order of tags in a message: order matters!  Misordering tags will result in a request that is invalid which will generate a fault response.

