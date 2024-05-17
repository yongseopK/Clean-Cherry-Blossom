import React, {useEffect, useRef} from 'react';

const CustomOverlay = ({ content, map, position, navermaps }) => {
    const overlayRef = useRef(null);

    useEffect(() => {
        const CustomOverlay = function () {
            navermaps.OverlayView.apply(this, arguments);
        };

        CustomOverlay.prototype = new navermaps.OverlayView();

        CustomOverlay.prototype.onAdd = function () {
            const div = document.createElement('div');
            div.style.position = 'absolute';
            div.style.left = '0';
            div.style.top = '0';
            div.style.backgroundColor = '#ffdae1';
            div.style.padding = '5px 10px';
            div.style.borderRadius = '5px';
            div.style.boxShadow = '0 2px 5px rgba(0, 0, 0, 0.3)';
            div.style.fontSize = '14px';
            div.style.fontWeight = 'bold';
            div.style.textAlign = 'center';
            div.style.width = '150px';
            div.style.zIndex = '100';
            div.innerHTML = `
                <span>${content.name}</span><br />
                <span>${content.date}</span>
            `;

            const overlayLayer = this.getPanes().overlayLayer;
            overlayLayer.appendChild(div);

            this.div = div;
        };

        CustomOverlay.prototype.draw = function () {
            const projection = this.getProjection();
            if (projection) {
                const point = projection.fromCoordToOffset(position);

                const div = this.div;
                div.style.left = point.x + 'px';
                div.style.top = point.y + 'px';
            }
        };

        CustomOverlay.prototype.onRemove = function () {
            this.div.parentNode.removeChild(this.div);
            this.div = null;
        };

        const overlay = new CustomOverlay();
        overlayRef.current = overlay;

        navermaps.Event.addListener(map, 'zoom_changed', function () {
            overlay.draw();
        });

        overlay.setMap(map);

        return () => {
            overlay.setMap(null);
        };
    }, [content, map, position, navermaps]);

    return null;
};

export default CustomOverlay;