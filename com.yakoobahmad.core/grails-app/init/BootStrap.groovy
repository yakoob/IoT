import com.yakoobahmad.config.GlobalConfig
import com.yakoobahmad.device.Light
import com.yakoobahmad.device.Smoke
import com.yakoobahmad.device.light.Hue
import com.yakoobahmad.media.ChristmasVideo
import com.yakoobahmad.media.HalloweenVideo
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
        // configureDataChristmas()

        /*
        Video.list() {
            println it.class.simpleName + " | " + it.name
        }
        */

    }

    private void configureDataHalloween(){

        // new Smoke(name: Smoke.Name.HALLOWEEN_REAR, position: 20, state: Smoke.State.OFF).save(failOnError:true, flush:true)
        // new Smoke(name: Smoke.Name.HALLOWEEN_REAR, position: 60, state: Smoke.State.ON).save(failOnError:true, flush:true)

        new HalloweenVideo(name: HalloweenVideo.Name.WOODS, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.GRIM_GRINNING_GHOST, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.KIDNAP_SANDY_CLAWS, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.MONSTER_MASH, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.WHATS_THIS, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.THIS_IS_HALLOWEEN, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.OOGIE_BOOGIE_PUMPKINS, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.TIMEWARP, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.THRILLER, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.GHOSTBUSTERS, type: HalloweenVideo.Type.PUMPKINS).save(failOnError:true, flush:true)

        new HalloweenVideo(name: HalloweenVideo.Name.SAM_NOCOSTUME, type: HalloweenVideo.Type.HOLOGRAM).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.SAM_SYMPHONY, type: HalloweenVideo.Type.HOLOGRAM).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.SAM_SCARE1, type: HalloweenVideo.Type.HOLOGRAM).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.SAM_SCARE2, type: HalloweenVideo.Type.HOLOGRAM).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.SAM_SCARE3, type: HalloweenVideo.Type.HOLOGRAM).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.SAM_SCARE4, type: HalloweenVideo.Type.HOLOGRAM).save(failOnError:true, flush:true)
        new HalloweenVideo(name: HalloweenVideo.Name.MINISTRY, type: HalloweenVideo.Type.DISABLED).save(failOnError:true, flush:true)

        new ColorHue(description: Color.Name.PURPLE, red: "0.7117647058823499", green: "0.9724025974025973", blue: "0.9042207792207793").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.BLUE, red: "0.6562091503267974", green: "0.9529220779220778", blue: "0.9334415584415585").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.GREEN, red: "0.27320261437908283", green: "0.9724025974025973", blue: "1").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.PINK, red: "0.8215686274509816", green: "1", blue: "1").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.ORANGE, red: "0.09542483660130567", green: "1", blue: "1").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.RED, red: "0.9797385620915028", green: "1", blue: "1").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.BLACK, red: "0", green: "0", blue: "0").save(failOnError:true, flush:true)
        new ColorHue(description: Color.Name.WHITE, red: "0", green: "0", blue: "1").save(failOnError:true, flush:true)

        new Hue(description: "Pumpkin_1", node: 8, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Front_Door", node: 16, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Garage_1", node: 9, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Garage_2", node: 10, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Garage_3", node: 11, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Door_1", node: 12, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Door_2", node: 13, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Door_3", node: 14, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)
        new Hue(description: "Door_4", node: 15, state: Light.State.OFF, color: ColorHue.findByDescription(Color.Name.ORANGE)).save(failOnError:true, flush:true)

    }

    private void configureDataChristmas(){

            new ChristmasVideo(name: ChristmasVideo.Name.DECK_THE_HALLS).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.GREAT_GIFT_WRAP).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.MARCH_WOODEN_SOLDIER).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.PACKING_SANTA_SLEIGH).save(failOnError:true, flush:true)
            new ChristmasVideo(name: ChristmasVideo.Name.TOY_TINKERING).save(failOnError:true, flush:true)

    }
}
