/*
 * Copyright (C) 2005-2007 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * and Open Source Software ("FLOSS") applications as described in Alfresco's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.web.bean.ml;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.alfresco.i18n.I18NUtil;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.ml.MultilingualContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.alfresco.web.bean.UserPreferencesBean;
import org.alfresco.web.bean.content.AddContentDialog;
import org.alfresco.web.bean.repository.Node;

/**
 * Dialog bean to upload a new document and to add it to an existing MLContainer.
 *
 * @author yanipig
 */
public class AddTranslationlDialog extends AddContentDialog
{

   private MultilingualContentService multilingualContentService;
   private UserPreferencesBean userPreferencesBean;

   // the multilingual container where to add this translation
   protected NodeRef mlContainer;

   // Locale of the new translation
   private String language;

   //  languages available in the ML container yet
   private SelectItem[] unusedLanguages;

   /* (non-Javadoc)
    * @see org.alfresco.web.bean.content.AddContentDialog#init(java.util.Map)
    */
   @Override
   public void init(Map<String, String> parameters)
   {
      super.init(parameters);

      this.language = null;
      setMlContainer(this.browseBean.getDocument().getNodeRef());
      setFileName(null);
      unusedLanguages = null;

   }

   /* (non-Javadoc)
    * @see org.alfresco.web.bean.content.AddContentDialog#finishImpl(javax.faces.context.FacesContext, java.lang.String)
    */
   @Override
   protected String finishImpl(FacesContext context, String outcome) throws Exception
   {

      // add the new file to the repository
      outcome = super.finishImpl(context, outcome);

      // add a new translation
      multilingualContentService.addTranslation(this.createdNode, this.mlContainer, I18NUtil.parseLocale(this.language));

      this.browseBean.setDocument(new Node(this.createdNode));

      return outcome;
   }

   @Override
   protected String getDefaultFinishOutcome()
   {

      return "showDocDetails";
   }

   @Override
   public String cancel()
   {
      super.cancel();

      return getDefaultFinishOutcome();
   }

   public boolean finishButtonDisabled()
   {
      return author == null || author.length() < 1 || language == null;
   }

   /**
    * @return the locale of this new translation
    */
   public String getLanguage()
   {
      return language;
   }

   /**
    * @param language the locale of this new translation
    */
   public void setLanguage(String language)
   {
      this.language = language;
   }

   /**
    * @return the Multilingual container where the translation will be associated
    */
   public NodeRef getMlContainer()
   {
      return mlContainer;
   }

   /**
    * Set the Multilingual container where the translation will be associated.
    *
    * @param mlContainer mlContainer is a MLDocument, the the MLContainer will
     * become it's own MLContainer
    */
   public void setMlContainer(NodeRef mlContainer)
   {
      QName type = null;

      if(mlContainer != null)
      {
         type = nodeService.getType(mlContainer);

         if(ContentModel.TYPE_MULTILINGUAL_CONTAINER.equals(type)){
            this.mlContainer = mlContainer;
         }
         else if(ContentModel.TYPE_CONTENT.equals(type)
               && nodeService.hasAspect(mlContainer, ContentModel.ASPECT_MULTILINGUAL_DOCUMENT)){

            this.mlContainer = multilingualContentService.getTranslationContainer(mlContainer);
         }
         else{
            this.mlContainer = null;
         }
      }
   }

   /**
    * @param unusedLanguages
    */
   public void setUnusedLanguages(SelectItem[] unusedLanguages)
   {
      this.unusedLanguages = unusedLanguages;
   }

   /**
    * Method calls by the dialog to limit the list of languages.
    *
    * @return the list of availables translation in the MLContainer
    */

   public SelectItem[] getUnusedLanguages()
   {

      if(unusedLanguages == null)
      {
         unusedLanguages = userPreferencesBean.getAvailablesContentFilterLanguages(getMlContainer(), false);
      }

      return unusedLanguages;
   }

   public MultilingualContentService getMultilingualContentService()
    {
        return multilingualContentService;
    }

    public void setMultilingualContentService(
            MultilingualContentService multilingualContentService)
    {
        this.multilingualContentService = multilingualContentService;
    }

   public UserPreferencesBean getUserPreferencesBean()
   {
      return userPreferencesBean;
   }

   public void setUserPreferencesBean(UserPreferencesBean userPreferencesBean)
   {
      this.userPreferencesBean = userPreferencesBean;
   }
}