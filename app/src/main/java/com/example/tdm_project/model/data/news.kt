package com.example.tdm_project.model.data

import android.os.Parcel
import android.os.Parcelable



data class news (
    var Title : String?,
    var Writer : String?,
    var Date : String,
    var Second_title : String?,
    var url : String,
    var Image : String?,
    var category : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Title)
        parcel.writeString(Writer)
        parcel.writeString(Date)
        parcel.writeString(Second_title)
        parcel.writeString(url)
        parcel.writeString(Image)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<news> {
        override fun createFromParcel(parcel: Parcel): news {
            return news(parcel)
        }

        override fun newArray(size: Int): Array<news?> {
            return arrayOfNulls(size)
        }
    }

}

fun getList ( ) : ArrayList<news>{
       var NewsList = ArrayList<news>()

    NewsList.addAll(listOf(news("Cyclone Vayu approaches India: Live updates - CNN","CNN","2019-06-13T08:28:00Z","Tropical Cyclone Vayu is barreling toward India's Gujarat coast. Six million people could be impacted by the storm. Follow here for the latest.",
        "https://www.cnn.com/world/live-news/cyclone-vayu-india-june-2019/index.html" ,"https://cdn.cnn.com/cnnnext/dam/assets/190612115553-cyclone-vayu-thursday-satellite-super-tease.jpg", "science"),
        news("Cyclone Vayu approaches India: Live updates - CNN","Bradford Betz","2019-06-13T07:32:20Z", "A U.K. maritime safety group warned early Thursday of an unspecified incident in the Gulf of Oman, urging “extreme caution” amid U.S.-Iran tensions.",
            "https://www.foxnews.com/world/uk-maritime-groups-warns-of-incident-in-gulf-of-oman" ,"https://static.foxnews.com/foxnews.com/content/uploads/2019/06/AP19158043580433.jpg","science"),
        news("'Counting On' star Grandma Mary Duggar's cause of death revealed - Fox News","Fox News","2019-06-13T07:28:51Z","Mary Duggar, paternal grandmother to the giant, infamous brood of Counting On fame, died at 78 in an accidental drowning at her home in in Springdale, Arkansas over the weekend, People reported.",
            "https://www.foxnews.com/entertainment/counting-on-star-grandma-mary-duggars-cause-of-death-revealed", "https://static.foxnews.com/foxnews.com/content/uploads/2019/06/Mary-Duggar-TLC.jpg" ,"politics"),

    news(" التشكيل المتوقع لمنتخب مصر أمام تنزانيا الليلة - اليوم السابع","محمد عبدو" ,"2019-06-13T08:28:00Z","استقر المكسيكى خافيير أجيرى، المدير الفنى للمنتخب الوطنى، على التشكيل الذى سيخوض به ودية تنزانيا المقرر لها التاسعة مساء اليوم فى إطار الإعداد لكأس الأمم الأفريقية 2019.",
     "https://www.youm7.com/story/2019/6/13/%D8%A7%D9%84%D8%AA%D8%B4%D9%83%D9%8A%D9%84-%D8%A7%D9%84%D9%85%D8%AA%D9%88%D9%82%D8%B9-%D9%84%D9%85%D9%86%D8%AA%D8%AE%D8%A8-%D9%85%D8%B5%D8%B1-%D8%A3%D9%85%D8%A7%D9%85-%D8%AA%D9%86%D8%B2%D8%A7%D9%86%D9%8A%D8%A7-%D8%A7%D9%84%D9%84%D9%8A%D9%84%D8%A9/4284218", "https://img.youm7.com/large/201811160716421642.jpg","sport"),
            news("Could Bitcoin’s Lightning Power Mobile Communications? This Startup Thinks So","Yahoo.com","17/03/2019T04:00:35Z","New research from mobile mesh networking company goTenna explores how bitcoin's lightning network can help decentralize mobile communications.", "https://finance.yahoo.com/news/could-bitcoin-lightning-power-mobile-040035592.html",
        "https://s.yimg.com/uu/api/res/1.2/THN.4e1p9GwpFVH6PSNGLw--~B/aD0xNzQyO3c9MjcxNztzbT0xO2FwcGlkPXl0YWNoeW9u/https://media.zenfs.com/en-US/coindesk_75/2ceb0582af3caa4932a01ac38ae3935d", "politics"),

        news( "Ethereum (ETH) Price Rallies Above $260: Turned Buy On Dips", " Aayush Jindal from Newsbtc.com ","2019-06-13T04:08:08Z",
            "ETH price started a strong upward move after it broke the key $250 resistance against the US Dollar. The price even broke the $255 and $260 resistance levels to move into a positive zone. There is a major bullish trend line forming with support near $252 on t…",
            "https://www.newsbtc.com/wp-content/uploads/2018/08/techanalysis-eth6-bullish.jpg","https://www.newsbtc.com/2019/06/13/ethereum-eth-price-rallies-above-260-turned-buy-on-dips/",
            "economics"),

                news("Crypto Market Starts Fresh Increase: Bitcoin Cash, BNB, EOS, TRX Price Analysis","Aayush Jindal from Newsbtc.com"
        ,"2019-06-13T05:00:35Z", "The total crypto market cap found support near $236.0B and jumped sharply above $245.0B. Bitcoin price broke the key $8,000 resistance level to move into a positive zone. EOS price broke the $6.40 and $6.50 resistance levels to start a decent recovery. Binanc…",
        "https://www.newsbtc.com/2019/06/13/crypto-market-starts-fresh-increase-bitcoin-cash-bnb-eos-trx-price-analysis/",
        "https://www.newsbtc.com/wp-content/uploads/2018/08/techanalysis-alts2.jpg", "economics"),

    news("Is bitcoin growing up? Regulated futures boom as investors seek a safer ride","CNA from Channelnewsasia.com" , "2019-06-13T06:16:56Z"
    ,"When bitcoin was born it was a symbol of counterculture, a rebel currency with near-anonymity and a lack of regulation. A decade later, there are growing signs it's entering the establishment its creators sought to subvert."
    ,"https://www.channelnewsasia.com/news/business/is-bitcoin-growing-up--regulated-futures-boom-as-investors-seek-a-safer-ride-11623672"
    ,"https://cna-sg-res.cloudinary.com/image/upload/q_auto,f_auto/image/11623668/16x9/991/557/a66e88d830a8affb61d4811c0163a823/aU/file-photo--an-electric-board-showing-exchange-rate-between-south-korean-won-and-bitcoin-at-a-cryptocurrencies-exchange-in-seoul-1.jpg"
    ,"economics"),

    news( "Apple's big iMovie iOS update includes green screen and more","Rachel England from Engadget", "2019-06-12T13:22:00Z"
        ,"It looks like Apple is taking the casual filmmaker a little more seriously. Its latest update for iMovie for iOS -- out today -- comes with a new green screen effect, better still image support, 80 new soundtracks, graphic overlays and more. The update's head…","https://www.engadget.com/2019/06/12/apple-imovie-ios-update-green-screen-more/",
        "https://o.aolcdn.com/images/dims?thumbnail=1200%2C630&quality=80&image_uri=https%3A%2F%2Fo.aolcdn.com%2Fimages%2Fdims%3Fcrop%3D1200%252C800%252C0%252C0%26quality%3D85%26format%3Djpg%26resize%3D1600%252C1067%26image_uri%3Dhttps%253A%252F%252Fs.yimg.com%252Fos%252Fcreatr-uploaded-images%252F2019-06%252F15d4d030-8cf7-11e9-9fb7-4599497115f3%26client%3Da1acac3e1b3290917d92%26signature%3Db6e566e1ab2c2eb40e2391716dcadf3f464ebd31&client=amp-blogside-v2&signature=1d39e0d1ff88acd32e1581dd8a9f1e46b3abf713",
        "tech"),

    news ("No, You Still Shouldn't Let Facebook Pay You to Track Your Phone Habits","Mike Epstein from Lifehacker.com","2019-06-12T19:30:00Z",
        "Facebook wants to know as much about you as possible—if you let it. This week, the social giant launched a new app-based market research program called Study from Facebook. If you participate, you’ll get paid to run an app on their your phone that tracks what…",
        "https://lifehacker.com/no-you-still-shouldnt-let-facebook-pay-you-to-track-yo-1835450016",
        "https://i.kinja-img.com/gawker-media/image/upload/s--2hTYym_u--/c_fill,fl_progressive,g_center,h_900,q_80,w_1600/iravkjbmamc3qewkthdp.jpg",
        "tech"),

    news ( "Google just revealed the Pixel 4 on Twitter","Nathan Ingraham from Engadget","2019-06-12T18:38:00Z"
        ,"Well, this is a new one. Google has decided to beat smartphone leakers at their own game by showing off the Pixel 4's back and camera on its own @madebygoogle Twitter account. The image shows a square-ish camera module, presumably one that will hold multiple …",
    "https://www.engadget.com/2019/06/12/google-pixel-4-camera-official-leak/",
    "https://o.aolcdn.com/images/dims?thumbnail=1200%2C630&quality=80&image_uri=https%3A%2F%2Fo.aolcdn.com%2Fimages%2Fdims%3Fresize%3D2000%252C2000%252Cshrink%26image_uri%3Dhttps%253A%252F%252Fs.yimg.com%252Fos%252Fcreatr-uploaded-images%252F2019-06%252Fcd2b2150-8d40-11e9-bdbb-e6bf52240aae%26client%3Da1acac3e1b3290917d92%26signature%3Dc1b84b063b786d5e6ae142541dee0877b4405223&client=amp-blogside-v2&signature=10d52f1a1e57524444c4c3e6c1fc46e44b070467",
    "tech"),

    news ("Le calendrier 2019/2020 de Premier League dévoilé - Foot Mercato","Footmercato.net","2019-05-14T8:02:54Z",
        "Sacré champion d’Angleterre pour la deuxième fois consécutive, Manchester City est prêt à repartir à la bataille. La Premier League vient de dévoiler le calendrier pour la saison 2019/2020. Et voici les dates principales.\nLes 20 clubs de Premier League (...)",
        "http://www.footmercato.net/premier-league/le-calendrier-2019-2020-de-premier-league-devoile_256383","http://www.footmercato.net/images/a/jurgen-klopp-et-pep-guardiola-lors-de-la-saison-2018-2019_256383.jpg","sport")
    ))
    return NewsList
   }





