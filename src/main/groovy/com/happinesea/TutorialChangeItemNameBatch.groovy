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
	header.licenseKey = '<license key>'
	header.serviceSecret = '<service secret>'
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
	return [
	    'alovivi-mask-house-01',
	    'horse-oil-soap_01',
	    'horse-oil-soap_03',
	    'horse-oil-soap_02',
	    'horse-oil-soap-hinoki_02',
	    'baiuntan-1',
	    'baiuntan-3',
	    'baiuntan-2',
	    'horse-oil-vanilla_01',
	    'horse-oil-rose_01',
	    'horse-oil-no7',
	    'horse-oil-clean_01',
	    'horse-oil-musk_01',
	    'horse-oil-gardenia_01',
	    'horse-oil-clean_02',
	    'alovivi-cleansing-sheet-01',
	    '24k-bueaty-bar-o_01',
	    'alovivi-mask-emollient-oil-01',
	    'gold-sakura-water-cream_01',
	    'alovivi-hat-lotion-07',
	    'alovivi-cleansing-lotion-03',
	    '5812-shampoo',
	    '5812-treatment',
	    '7gf-rich-gold-fackmask',
	    'lavender-water-cream_02-a',
	    'dr-water-02',
	    'tsutsuhari-200mt-01',
	    'raise-solution-sc-01',
	    'sakura-uv-mist-1',
	    'alovivi-mask-royaljelly-01',
	    'rear-seat-grapefruit',
	    'rear-seat-lavender',
	    'rear-seat-mugwort',
	    'rear-seat-titanium',
	    'beauty-bar-bm2',
	    'rear-seat-hot',
	    'tsutsuhari-03-a',
	    'tsutsuhari-05',
	    'tsutsuhari-03',
	    'new-sakura-water-cream_01',
	    'egf-undiluted-solution-02',
	    'egf-undiluted-solution-01',
	    'egf-undiluted-solution-03',
	    'lavender-horseoil-cream_01_s',
	    'lavender-horseoil-cream_01',
	    'sakura-egf-peeling-gel_01',
	    'sakura-peeling-gel_01',
	    'alovivi-hat-lotion-01',
	    'jitsuhari150-01',
	    'alovivi-mask-hpc-01',
	    'sakura-horseoil-cream_01',
	    'lavender-water-cream_02',
	    'stretch-eye-mask-02',
	    'stretch-eye-mask-01',
	    'lavender-water-cream_01',
	    'tsutsuhari-02',
	    'alovivi-hat-balm-01',
	    'alovivi-mask-pearl-01',
	    'alovivi-cleansing-lotion-01',
	    'jitsuhari-01',
	    'jitsuhari-03',
	    'horse-oil-chamaecyparis_01',
	    'spa-cleaning-gel-pack-01',
	    'alovivi-triple-lotion-01',
	    'jitsuhari-02',
	    'alovivi-proteoglycan-01',
	    'sakura-water-cream_03',
	    'sakura-water-cream_01',
	    'sakura-water-cream_02',
	    'dr-water-01',
	    '24k-bueaty-bar-t_01',
	    'muscat-vinegar-bottle_05',
	    'sakura-egf-water-cream_01',
	    'sakura-egf-water-cream_02',
	    'sakura-egf-water-cream_03',
	    'sakura-egf-water-cream_04',
	    'tsutsuhari-01',
	    '24k-bueaty-bar-t_01-a',
	    'sakura-egf-water-cream_01-a',
	    'sakura-egf-water-cream_01_s1_20',
	    '7gf-eye-and-lip-veil-serum',
	    '7gf-lift-serum-ex',
	    '7gf-moist-lift-lotion',
	    '7gf-revital-cream-ex_s',
	    '7gf-lift-serum-ex_s',
	    '7gf-moist-lift-lotion_s',
	    '7gf-eye-and-lip-veil-serum_s',
	    '7gf-revital-cream-ex',
	    '7gf-revital-cream-ex_s1',
	    '24k-bueaty-bar-t_01_s1_15',
	    '24k-bueaty-bar-o_01_s1_20',
	    'jyueki-sheet-01-s',
	    'jyueki-sheet-01',
	    'sakura-egf-moisture-wash-01',
	    'sakura-egf-bb-cream-03',
	    'sakura-egf-bb-cream-01',
	    'alovivi-hat-balm-06',
	    'alovivi-triple-lotion-06',
	    'muscat-vinegar-bottle_04',
	    'sakura-egf-water-peeling-set-_01',
	    'biomecha-kne',
	    'lavender-water-cream_01_s',
	    'sakura-water-cream_01_c',
	    'p-shinake-mask_01',
	    'muscat-vinegar-bottle_01',
	    'jitsuhari-01_s',
	    'muscat-vinegar-bottle_02',
	    'muscat-vinegar-bottle_03',
	    'tsutsuhari-01_s',
	    'sakura-egf-gift-01',
	    '7gf-gift',
	    'gold-sakura-water-cream_01_s',
	    'sakura-cleansing-foam_01',
	    'sakura-egf-peeling-gel_01_s'
	];
    }
}
