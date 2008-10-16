package com.msi.manning.restaurant.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Handler impl for Google Base API and restaurant Review bean.
 * 
 * @author charliecollins
 * 
 */
public class ReviewHandler extends DefaultHandler {

    private static final String CLASSTAG = ReviewHandler.class.getSimpleName();
    private static final String ENTRY = "entry";
    private static final String R_NAME = "name_of_item_reviewed";
    private static final String R_AUTHOR = "review_author";
    private static final String R_LINK = "link";
    private static final String R_IMAGE_LINK = "image_link"; 
    private static final String R_LOCATION = "location";
    private static final String R_RATING = "rating";
    private static final String R_PHONE = "phone_of_item_reviewed";
    private static final String R_DATE = "review_date";
    private static final String R_CONTENT = "content";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private boolean startEntry;
    private int numEntries;
    private boolean nameChars;
    private boolean authorChars;
    private boolean locationChars;
    private boolean ratingChars;
    private boolean phoneChars;
    private boolean dateChars;
    private boolean contentChars;
    private boolean imageLinkChars;
    private final ArrayList<Review> reviews;
    private Review review;

    public ReviewHandler() {
        this.reviews = new ArrayList<Review>();
    }

    @Override
    public void startDocument() throws SAXException {
    }
    
    // Note that Android 1.0 has potential bugs relating to SAX parsing, setting the namespace and namespace-prefix 
    // features result in error, and "qName" is always empty - have to use localName (was the opposite pre 1.0)
    @Override
    public void startElement(final String namespaceURI, final String localName, final String qName,
            final Attributes atts) throws SAXException {

        ///Log.v(Constants.LOGTAG, " " + ReviewHandler.CLASSTAG + " qName - " + qName + " localName - " + localName);

        if (localName.equals(ReviewHandler.ENTRY)) {
            this.startEntry = true;
            this.review = new Review();
        }

        if (this.startEntry) {
            if (localName.equals(ReviewHandler.R_NAME)) {
                this.nameChars = true;
            } else if (localName.equals(ReviewHandler.R_AUTHOR)) {
                this.authorChars = true;
            } else if (localName.equals(ReviewHandler.R_LINK)) {
                String rel = this.getAttributeValue("rel", atts);
                if (rel != null && rel.equals("alternate")) {
                    this.review.link = this.getAttributeValue("href", atts);
                }                
            } else if (localName.equals(ReviewHandler.R_LOCATION)) {
                this.locationChars = true;
            } else if (localName.equals(ReviewHandler.R_RATING)) {
                this.ratingChars = true;
            } else if (localName.equals(ReviewHandler.R_PHONE)) {
                this.phoneChars = true;
            } else if (localName.equals(ReviewHandler.R_DATE)) {
                this.dateChars = true;
            } else if (localName.equals(ReviewHandler.R_CONTENT)) {
                this.contentChars = true;
            } else if (localName.equals(ReviewHandler.R_IMAGE_LINK)) {
                this.imageLinkChars = true;
            }
        }
    }    
    
    @Override
    public void characters(final char[] ch, final int start, final int length) {
        String chString = "";
        if (ch != null) {
            chString = new String(ch, start, length);
        }
        
        ///Log.v(Constants.LOGTAG, " " + ReviewHandler.CLASSTAG + " chString = " + chString);
        if (this.startEntry) {
            if (this.nameChars) {
                this.review.name = chString;
            } else if (this.authorChars) {
                this.review.author = chString;
            } else if (this.locationChars) {
                this.review.location = chString;
            } else if (this.ratingChars) {
                this.review.rating = chString;
            } else if (this.phoneChars) {
                this.review.phone = chString;
            } else if (this.dateChars) {
                if (ch != null) {
                    try {
                        this.review.date = ReviewHandler.DATE_FORMAT.parse(chString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else if (this.contentChars) {
                this.review.content = new String(chString);
            } else if (this.imageLinkChars) {
                this.review.imageLink = new String(chString);
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(final String namespaceURI, final String localName, final String qName) throws SAXException {
        if (localName.equals(ReviewHandler.ENTRY)) {
            this.startEntry = false;
            this.numEntries++;
            this.reviews.add(this.review);
        }

        if (this.startEntry) {
            if (localName.equals(ReviewHandler.R_NAME)) {
                this.nameChars = false;
            } else if (localName.equals(ReviewHandler.R_AUTHOR)) {
                this.authorChars = false;
            } else if (localName.equals(ReviewHandler.R_LOCATION)) {
                this.locationChars = false;
            } else if (localName.equals(ReviewHandler.R_RATING)) {
                this.ratingChars = false;
            } else if (localName.equals(ReviewHandler.R_PHONE)) {
                this.phoneChars = false;
            } else if (localName.equals(ReviewHandler.R_DATE)) {
                this.dateChars = false;
            } else if (localName.equals(ReviewHandler.R_CONTENT)) {
                this.contentChars = false;
            } else if (localName.equals(ReviewHandler.R_IMAGE_LINK)) {
                this.imageLinkChars = false;
            }
        }
    }

    public ArrayList<Review> getReviews() {
        return this.reviews;
    }

    private String getAttributeValue(String attName, Attributes atts) {
        String result = null;
        for (int i = 0; i < atts.getLength(); i++) {
            String thisAtt = atts.getLocalName(i);
            if (attName.equals(thisAtt)) {
                result = atts.getValue(i);
                break;
            }
        }
        return result;
    }
}
