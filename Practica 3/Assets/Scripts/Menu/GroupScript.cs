using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GroupScript : MonoBehaviour
{
    LevelPackage[] levelPackages;

    //Arrays de botones
    MenuButton[] groupButtons;
    LevelButton[] levelButtons;
    
    //Prefabs
    public MenuButton groupButton;
    public LevelButton levelButton;

    //Posicion de containers
    public Transform groupContainer;
    public Transform levelContainer;

    //Para la selección de niveles
    public GameObject levelSelection;
    public Text levelSelectionTitle;

    // Start is called before the first frame update
    void Start()
    {
        levelPackages = GameManager.Instance()._levelPacks;
        groupButtons = new MenuButton[levelPackages.Length];

        for (int i = 0; i < groupButtons.Length; i++) //Asi no depende de valores, por si se añaden más packs
        {
            groupButtons[i] = CreateGroupButton(levelPackages[i], i);
        }
    }

    public MenuButton CreateGroupButton(LevelPackage levelPackage, int i)
    {
        MenuButton butt = Instantiate(groupButton, groupContainer); //A base de prefabs

        SpriteState spriteState = new SpriteState();
        spriteState.pressedSprite = levelPackage._pressedButton; //Para poner animacion bonita de presionado

        butt.setUp(levelPackage._button, spriteState, levelPackage._packName, this, i); //Configuración del boton
        return butt;
    }

    public void ShowLevels(int i)
    {
        //Se desactiva el canvas de grupos de niveles
        groupContainer.gameObject.SetActive(false);

        //Se activa el canvas del grupo elegido, y se pone el texto
        levelSelection.SetActive(true);
        levelSelectionTitle.text = levelPackages[i]._packName;

        //Creación de botones, ya que se empieza con el menu sin ellos
        int totalLevels = levelPackages[i]._levels.Length;
        levelButtons = new LevelButton[totalLevels];

        for (int j = 0; j < levelButtons.Length; j++) //Asi no depende de valores, por si se añaden más packs
        {
            levelButtons[j] = CreateLevelButton(j, levelPackages[i]._colorScheme, i);
        }
    }

    public LevelButton CreateLevelButton(int number, Color colorScheme, int pack)
    {
        LevelButton butt = Instantiate(levelButton, levelContainer); //A base de prefabs

        int levelComp = GameManager.Instance().GetLevelCompleted(pack);

        //Comprobacion de valores
        bool done = number < levelComp;
        bool available = number <= levelComp;

        butt.setUp(available, number + 1, colorScheme, pack, done); //Configuración del boton
        return butt;
    }
}
