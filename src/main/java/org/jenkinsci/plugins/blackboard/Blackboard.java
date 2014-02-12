package org.jenkinsci.plugins.blackboard;

import hudson.model.*;
import hudson.Extension;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import hudson.views.ViewsTabBar;
import hudson.views.ViewsTabBarDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * 
 * @author Stefan Bauer
 *
 */

public class Blackboard extends ViewsTabBar {
   
    @DataBoundConstructor
    public Blackboard() {
        super();
    }
    
    @Extension
    public static final class CustomViewsTabBarDescriptor extends ViewsTabBarDescriptor {
       

        @Override		// is called by Jenkins when the global config is changed and the user hits the “apply” or “save” button
        public boolean configure(StaplerRequest req,
        		JSONObject formData) throws FormException {
        	labelText = formData.getString("labelText");
        	tabColour = formData.getString("tabColour");
        	
        	save();		// to persist the config data into the xml file, managed by Jenkins
            return false;
        }       
        
        public CustomViewsTabBarDescriptor() {
            load();		// loads the configuration data from an XML file managed by Jenkins
        }

                            
        
        @Override
        public String getDisplayName() {	
            return "Blackboard";
        }
        
        
     /**
      *		setter methods:		- content for global.jelly text field
      */          
        private String labelText = "default_text";
        private String tabColour = "default_color";		
                          
        public String getLabelText(){
            return labelText;
        } 
        
        public String getTabColour(){
            return tabColour;
        } 
        
        public ListBoxModel doFillTabColourItems(){		// content for global.jelly drop down list
            return new ListBoxModel(
                new Option("Green", "00ff00", tabColour.equals("00ff00")),
                new Option("Yellow", "ffff00", tabColour.equals("ffff00")),
                new Option("Red", "ff0000", tabColour.equals("ff0000")));
        }                  
    }
    
    
    /**	getter methods:		- matching the configuration items implied by the entries in our global.jelly
	  * 					- global.jelly will call this with the 'it' element		*/   
    public String getTabLabel(){  
   	 	CustomViewsTabBarDescriptor descriptor = (CustomViewsTabBarDescriptor) super.getDescriptor();    	
   	 	return descriptor.getLabelText();
    }
    
    public String getTabColour(){  
   	 	CustomViewsTabBarDescriptor descriptor = (CustomViewsTabBarDescriptor) super.getDescriptor();    	
   	 	return descriptor.getTabColour();
    }   
    
    public int getFailureCount(View v) {
        int failed = 0;
        for (TopLevelItem item : v.getItems()) { 	// getItems: Gets all the items in this collection in a read-only view
            for (Job j : item.getAllJobs()) {		// getAllJobs: Gets all the jobs that this Item contains as descendants			
              switch (j.getIconColor()) {		// getIconColor: Used as the color of the status ball for the project
                case RED:
                  failed++;
                  break;
                case BLUE:
                case YELLOW:
                case DISABLED:
                default:
                  break;
              }
            }
        }
        return failed;
    }

}