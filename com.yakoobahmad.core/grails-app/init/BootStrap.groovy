import com.yakoobahmad.config.GlobalConfig
import com.yakoobahmad.device.Light
import com.yakoobahmad.device.Smoke
import com.yakoobahmad.media.ChristmasVideo
import com.yakoobahmad.media.HalloweenVideo
import com.yakoobahmad.halloween.light.Hue
import com.yakoobahmad.visualization.Color
import com.yakoobahmad.visualization.ColorHue

class BootStrap implements GlobalConfig {

    def akkaService
    def mqttClientService

    def init = { servletContext ->
        configureModels()
        akkaService.init()
        mqttClientService.init()
    }

    def destroy = {}

    private void configureModels(){

        configureDataHalloween()
        configureDataChristmas()

        /*
        Video.list() {
            println it.class.simpleName + " | " + it.name
        }
        */

    }

    private void configureDataHalloween(){
            /*
            new Smoke(name: Smoke.Name.HALLOWEEN_REAR, position: 20, state: Smoke.State.OFF).save(failOnError:true, flush:true)
            new Smoke(name: Smoke.Name.HALLOWEEN_REAR, position: 60, state: Smoke.State.ON).save(failOnError:true, flush:true)

            new HalloweenVideo(name: HalloweenVideo.Name.WOODS).save(failOnError:true, flush:true)
            new HalloweenVideo(name: HalloweenVideo.Name.GRIM_GRINNING_GHOST).save(failOnError:true, flush:true)
            new HalloweenVideo(name: HalloweenVideo.Name.KIDNAP_SANDY_CLAWS).save(failOnError:true, flush:true)
            new HalloweenVideo(name: HalloweenVideo.Name.MONSTER_MASH).save(failOnError:true, flush:true)
            new HalloweenVideo(name: HalloweenVideo.Name.WHATS_THIS).save(failOnError:true, flush:true)
            new HalloweenVideo(name: HalloweenVideo.Name.THIS_IS_HALLOWEEN).save(failOnError:true, flush:true)
            new HalloweenVideo(name: HalloweenVideo.Name.OOGIE_BOOGIE_PUMPKINS).save(failOnError:true, flush:true)

            new ColorHue(description: Color.Name.PURPLE, red: "0.7117647058823499", green: "0.9724025974025973", blue: "0.9042207792207793").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.BLUE, red: "0.6562091503267974", green: "0.9529220779220778", blue: "0.9334415584415585").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.GREEN, red: "0.27320261437908283", green: "0.9724025974025973", blue: "1").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.PINK, red: "0.8215686274509816", green: "1", blue: "1").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.ORANGE, red: "0.09542483660130567", green: "1", blue: "1").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.RED, red: "0.9797385620915028", green: "1", blue: "1").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.BLACK, red: "0", green: "0", blue: "0").save(failOnError:true, flush:true)
            new ColorHue(description: Color.Name.WHITE, red: "0", green: "0", blue: "1").save(failOnError:true, flush:true)

            new Hue(node: 1, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            new Hue(node: 2, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            new Hue(node: 3, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            new Hue(node: 4, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            new Hue(node: 5, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            new Hue(node: 6, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            new Hue(node: 7, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.GREEN)).save(failOnError:true, flush:true)
            */
    }

    private void configureDataChristmas(){

            new ChristmasVideo(name: ChristmasVideo.Name.DECK_THE_HALLS).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.GREAT_GIFT_WRAP).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.MARCH_WOODEN_SOLDIER).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.PACKING_SANTA_SLEIGH).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.TOY_TINKERING).save(failOnError:true, flush:true)

    }
}
