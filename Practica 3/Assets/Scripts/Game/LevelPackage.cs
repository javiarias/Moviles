﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Objeto que contiene los datos importantes a un paquete de niveles
/// </summary>
[CreateAssetMenu(fileName = "LevelPackage", menuName = "ScriptableObjects/LevelPackage", order = 2)]
public class LevelPackage : ScriptableObject
{

    public TextAsset[] _levels;

    public Sprite _button;
    public Sprite _pressedButton;

    public Color _colorScheme;
    public Color _hintColorScheme;

    public string _packName = "";
}
