using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraScaling : MonoBehaviour
{
    public float sceneSize = 10;

    /// <summary>
    /// De esta forma solo se actualiza en el editor, para testear cosas.
    /// Normalmente la resolución no va a cambiar constantemente, y el juego no soporta rotación de pantalla
    /// </summary>
#if UNITY_EDITOR
    private void Update()
    {
        if (_camera)
        {
            if (Screen.width > Screen.height)
            {
                float unitsPerPixel = sceneSize / Screen.height;

                float desiredHalfHeight = 0.5f * unitsPerPixel * Screen.width;

                _camera.orthographicSize = desiredHalfHeight;
            }
            else
            {
                float unitsPerPixel = sceneSize / Screen.width;

                float desiredHalfHeight = 0.5f * unitsPerPixel * Screen.height;

                _camera.orthographicSize = desiredHalfHeight;
            }
        }
    }
#endif

    public Camera _camera;
    void Start()
    {
        if (_camera)
        {
            if (Screen.width > Screen.height)
            {
                float unitsPerPixel = sceneSize / Screen.height;

                float desiredHalfHeight = 0.5f * unitsPerPixel * Screen.width;

                _camera.orthographicSize = desiredHalfHeight;
            }
            else
            {
                float unitsPerPixel = sceneSize / Screen.width;

                float desiredHalfHeight = 0.5f * unitsPerPixel * Screen.height;

                _camera.orthographicSize = desiredHalfHeight;
            }
        }
    }
}
