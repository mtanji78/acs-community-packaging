<?xml version="1.0" encoding="UTF-8"?>
<OpenSearchDescription xmlns="http://a9.com/-/spec/opensearch/1.1/" xmlns:alf="http://www.alfresco.org">
  <ShortName>Alfresco Text Search</ShortName>
  <LongName>Alfresco ${agent.edition} Text Search ${agent.version}</LongName>
  <Description>Search Alfresco "company home" using text keywords</Description>
  <#comment>IE takes first template from list, thus html response is listed first</#comment>
  <Url type="text/html" template="${request.servicePath}/search/text?q={searchTerms}&amp;p={startPage?}&amp;c={count?}&amp;l={language?}&amp;guest={alf:guest?}"/>
  <Url type="application/atom+xml" template="${request.servicePath}/search/text?q={searchTerms}&amp;p={startPage?}&amp;c={count?}&amp;l={language?}&amp;guest={alf:guest?}&amp;format=atom"/>
  <Url type="application/rss+xml" template="${request.servicePath}/search/text?q={searchTerms}&amp;p={startPage?}&amp;c={count?}&amp;l={language?}&amp;guest={alf:guest?}&amp;format=rss"/>
  <Image height="16" width="16" type="image/x-icon">${request.path}/images/logo/AlfrescoLogo16.ico</Image>
</OpenSearchDescription>