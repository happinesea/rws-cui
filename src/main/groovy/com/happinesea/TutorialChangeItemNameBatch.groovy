package com.happinesea;

import org.apache.commons.beanutils.BeanUtils

import com.happinesea.ec.rws.lib.RwsCrawler
import com.happinesea.ec.rws.lib.bean.form.rakuten.RwsItemApiSearchForm
import com.happinesea.ec.rws.lib.bean.form.rakuten.RwsItemApiUpdateItemListForm
import com.happinesea.ec.rws.lib.bean.form.rakuten.RwsItemApiUpdateItemListForm.ItemsUpdateRequest
import com.happinesea.ec.rws.lib.bean.rakuten.RwsRequestHeaderBean
import com.happinesea.ec.rws.lib.bean.rakuten.enumerated.ItemApiResponseCodeEnum
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsErrorMessage
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsItem
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsItemRequest
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsItemSearchResponseResult
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsItemSearchResult
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsItemUpdateResult
import com.happinesea.ec.rws.lib.bean.rakuten.node.RwsItemsUpdateResult
import com.happinesea.ec.rws.lib.rakuten.RwsCrawlerItemSearchApi
import com.happinesea.ec.rws.lib.rakuten.RwsCrawlerItemsUpdateApi

import groovy.util.logging.Log4j2

@Log4j2
public class TutorialChangeItemNameBatch {
    static String newStr = ""
    static String regex = /^\[最大2000円OFFクーポン\]/
    static int processCount = 10


    public static void main(String[] arg) {
	RwsCrawler crawler = new RwsCrawler()

	RwsRequestHeaderBean header = new RwsRequestHeaderBean()
	header.licenseKey = 'SL319828_4PKg7XxQ7lJAVY7o'
	header.serviceSecret = 'SP319828_I66gqrdft2BxMjRc'
	RwsCrawlerItemSearchApi itemSearchApi = new RwsCrawlerItemSearchApi(header)
	itemSearchApi.crawler = crawler
	RwsCrawlerItemsUpdateApi itemsUpdateApi = new RwsCrawlerItemsUpdateApi(header)
	itemsUpdateApi.crawler = crawler
	try {

	    RwsItemApiSearchForm form = new RwsItemApiSearchForm()
	    RwsItemApiUpdateItemListForm updateForm = null


	    for(int i = 1; i <= getUrl().length;i++) {

		form.itemUrl = getUrl()[i-1]

		RwsItemSearchResponseResult itemSearchResponseResult = itemSearchApi.run(form)
		RwsItemSearchResult itemSearchResult = itemSearchResponseResult.itemSearchResult


		int point = i%processCount
		if(point == 1) {
		    updateForm = new RwsItemApiUpdateItemListForm()
		    updateForm.itemsUpdateRequest = new ItemsUpdateRequest()
		    updateForm.itemsUpdateRequest.items = new ArrayList<RwsItemRequest>()
		}

		for(RwsItem item in itemSearchResult.items) {
		    item.itemName = item.itemName.replaceAll(regex , newStr)
		    println  "item -> ${item.itemName}"
		    RwsItemRequest tmp = new RwsItemRequest()
		    BeanUtils.copyProperties(tmp, item)
		    updateForm.itemsUpdateRequest.items.add(tmp)
		}

		if(point == 0 || i == getUrl().length) {
		    println "process -> ${i} ->${updateForm.itemsUpdateRequest.items.size()}"
		    RwsItemsUpdateResult updateResult = itemsUpdateApi.run(updateForm)
		    for(RwsItemUpdateResult itemUpdateResult in updateResult.itemsUpdateResult) {
			println "itemUpdateResult -> ${itemUpdateResult.code} "
			if(ItemApiResponseCodeEnum.N000 != itemUpdateResult.code) {
			    println "${itemUpdateResult.item.itemUrl} -> errormessage: "
			    for ( RwsErrorMessage message in itemUpdateResult.errorMessages) {
				println "message -> ${message.msg} "
			    }
			}
		    }
		}

		this.sleep(500)
	    }
	}catch(Exception e) {
	    e.printStackTrace()
	}
    }

    static String[] getUrl() {
	return ['item-01', 'item-02',];
    }
}
