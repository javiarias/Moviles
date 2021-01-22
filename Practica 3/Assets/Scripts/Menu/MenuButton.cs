using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MenuButton : MonoBehaviour
{
    // Componentes del botón como tal
    public Image image;
    public Button button;
    public Text text;
    public Text percentageText;

    //Indice
    int i;

    //Menu
    GroupScript menu;

    public void setUp(Sprite button_, SpriteState pressedButton_, string packName_, GroupScript groupScript_, int i_)
    {
        image.sprite = button_;
        button.spriteState = pressedButton_;
        text.text = packName_;
        i = i_;

        percentageText.text = ((GameManager.Instance().GetLevelCompleted(i_) / (float)GameManager.Instance().GetLevelTotal(i_) * 100.0f)).ToString("F2") + "%";

        menu = groupScript_;
    }

    public void OnClick()
    {
        menu.ShowLevels(i);
    }
}
